package swapnil.meta.gossip

import java.nio.ByteBuffer

import boopickle.Default._
import swapnil.meta.NodeIdentity

sealed trait GossipMessage

case class NodeVersion(identity: NodeIdentity, version: String)

case class SynGossipMessage(knownNodeVersions: Map[NodeIdentity, String]) extends GossipMessage {
  def deserialize(): Array[Byte] = {
    Pickle.intoBytes[SynGossipMessage](this).array()
  }
}

object SynGossipMessage {
  def parse(bytes: Array[Byte]): SynGossipMessage = {
    Unpickle.apply[SynGossipMessage].fromBytes(ByteBuffer.wrap(bytes))
  }
}

case class SynAckGossipMessage(updatedNodes: Set[NodeVersion], sendMe: Set[NodeIdentity]) extends GossipMessage {
  def deserialize(): Array[Byte] = {
    Pickle.intoBytes[SynAckGossipMessage](this).array()
  }
}

object SynAckGossipMessage {
  def parse(bytes: Array[Byte]): SynAckGossipMessage = {
    Unpickle.apply[SynAckGossipMessage].fromBytes(ByteBuffer.wrap(bytes))
  }
}

case class SynAck2GossipMessage(data: Set[NodeVersion]) extends GossipMessage {
  def deserialize(): Array[Byte] = {
    Pickle.intoBytes[SynAck2GossipMessage](this).array()
  }
}

object SynAck2GossipMessage {
  def parse(bytes: Array[Byte]): SynAck2GossipMessage = {
    Unpickle.apply[SynAck2GossipMessage].fromBytes(ByteBuffer.wrap(bytes))
  }
}
