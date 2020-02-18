package swapnil.meta.gossip.handlers

import java.io.DataInputStream

import swapnil.meta.NodeState
import swapnil.meta.gossip.{MessageProtocol, SynAck2GossipMessage}
import swapnil.meta.utils.RichUtils.RichString

class SynAck2Handler(nodeState: NodeState, messageProtocol: MessageProtocol) {
  def handle(inputStream: DataInputStream): Unit ={
    val synAck2GossipMessage = SynAck2GossipMessage.parse(messageProtocol.readMessage(inputStream))

    handleSynAck2(synAck2GossipMessage)
  }

  private def handleSynAck2(synAck2GossipMessage: SynAck2GossipMessage): Unit = {
    synAck2GossipMessage.data.foreach(nv => {
      nodeState.updateNodeInfo(nv._1, nv._2.toDate)
      println(s"INFO: [${nodeState.selfIdentity}] Handled SynAck2: updated $nv")
    })
  }
}
