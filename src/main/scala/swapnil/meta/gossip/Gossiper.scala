package swapnil.meta.gossip

import java.io.DataOutputStream
import java.net.InetSocketAddress

import swapnil.meta.{NodeIdentity, NodeState}
import swapnil.meta.communication.{SimpleSocketClient, SocketClientFactory}
import swapnil.meta.utils.RichUtils._

class Gossiper(nodeState: NodeState, socketClientFactory: SocketClientFactory, messageProtocol: MessageProtocol) {
  def initGossipWith(node: NodeIdentity): Unit = {
    val simpleSocketClient: SimpleSocketClient = socketClientFactory.newSocketClient
    val address = new InetSocketAddress(node.inetAddress, node.gossipPort)
    val (inputStream, outputStream) = simpleSocketClient.init(address)

    println(s"INFO: [${nodeState.selfIdentity}] Sending Syn message to $node")
    sendSyn(outputStream)

    val synAckGossipMessage = SynAckGossipMessage.parse(messageProtocol.readMessage(inputStream))
    println(s"INFO: [${nodeState.selfIdentity}] SynAck received from $node")

    synAckGossipMessage.updatedNodes.foreach(nv => {
      nodeState.updateNodeInfo(nv._1, nv._2.toDate)
      println(s"INFO: [${nodeState.selfIdentity}] Handled SynAck: updated $nv")
    })

    sendSynAck2(synAckGossipMessage.sendMe, outputStream)
    println(s"INFO: [${nodeState.selfIdentity}] Sending SynAck2 message to $node")

    simpleSocketClient.close()
  }

  private def sendSyn(outputStream: DataOutputStream): Unit = {
    val selfVersion = (nodeState.selfIdentity, nodeState.fetchVersion().toFormattedDateString)
    val nodeVersions = nodeState.fetchAllAvailableVersions().map(a => (a._1, a._2.toFormattedDateString)) + selfVersion

    val message = SynGossipMessage(nodeVersions)

    val bytes = message.deserialize()
    messageProtocol.writeMessage(outputStream, bytes)
  }

  private def sendSynAck2(requestedDataFor: Set[NodeIdentity], dataOutputStream: DataOutputStream): Unit = {
    val requestedData = nodeState.fetchVersionsOf(requestedDataFor)

    val synAck2 = SynAck2GossipMessage(requestedData)
    messageProtocol.writeMessage(dataOutputStream, synAck2.deserialize())
  }
}
