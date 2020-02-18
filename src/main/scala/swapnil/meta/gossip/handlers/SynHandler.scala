package swapnil.meta.gossip.handlers

import java.io.{DataInputStream, DataOutputStream}
import java.util.Date

import swapnil.meta.gossip.{MessageProtocol, SynAckGossipMessage, SynGossipMessage}
import swapnil.meta.utils.RichUtils._
import swapnil.meta.{NodeIdentity, NodeState}

class SynHandler(nodeState: NodeState, messageProtocol: MessageProtocol) {
  def handle(inputStream: DataInputStream, outputStream: DataOutputStream): Unit = {
    val synBytes = messageProtocol.readMessage(inputStream)
    val synMessage = SynGossipMessage.parse(synBytes)

    replyWithSynAck(synMessage, outputStream)
  }

  private def replyWithSynAck(message: SynGossipMessage, outputStream: DataOutputStream): Unit = {
    val receivedNodeVersions = message.knownNodeVersions
    val kv: (NodeIdentity, Date) = (nodeState.selfIdentity, nodeState.fetchVersion())
    val knownNodeVersions = nodeState.fetchAllAvailableVersions().+(kv)

    val sendMe = calculateUpdatedNodesClientHas(receivedNodeVersions, knownNodeVersions)
    val updatedNodes = calculateUpdatedNodesThisNodeHas(knownNodeVersions, receivedNodeVersions)

    val synAckGossipMessage: SynAckGossipMessage = SynAckGossipMessage(updatedNodes, sendMe.keySet)
    messageProtocol.writeMessage(outputStream, synAckGossipMessage.deserialize())
  }

  private def calculateUpdatedNodesThisNodeHas(knownNodeVersions: Map[NodeIdentity, Date], receivedNodeVersions: Map[NodeIdentity, String]) = {
    knownNodeVersions.filter {
      case (identity, knownVersion) =>
        val maybeString = receivedNodeVersions.get(identity)

        val senderDoesNotKnowThisNode = maybeString.isDefined
        if (senderDoesNotKnowThisNode) {
          val receivedVersion = maybeString.get.toDate
          receivedVersion.before(knownVersion)
        } else {
          true
        }
    }.map(kv => (kv._1, kv._2.toFormattedDateString))
  }

  private def calculateUpdatedNodesClientHas(receivedNodeVersions: Map[NodeIdentity, String], knownNodeVersions: Map[NodeIdentity, Date]) = {
    receivedNodeVersions.filter {
      case (identity, version) =>
        val receivedVersion = version.toDate
        val knownVersion = knownNodeVersions.get(identity)

        !(knownVersion.isDefined && knownVersion.get.after(receivedVersion))
    }
  }
}
