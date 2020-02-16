package swapnil.meta.gossip

import java.nio.ByteBuffer

import boopickle.Default._

sealed trait GossipMessageType

sealed trait GossipMessage

case class NodeVersion(ni: NodeIdentity, version: String)

case class SynGossipMessage(knownNodeVersions: Set[NodeVersion]) extends GossipMessage {
  private val TYPE_CODE: Byte = 0.byteValue()

  def deserialize(): Array[Byte] = {
    TYPE_CODE +: Pickle.intoBytes(this).array()
  }
}

case class SynAckGossipMessage(updatedNodes: Set[NodeVersion], sendMe: Set[NodeVersion]) extends GossipMessage {
  private val TYPE_CODE: Byte = 1.byteValue()

  def deserialize(): Array[Byte] = {
    TYPE_CODE +: Pickle.intoBytes(this).array()
  }
}

object GossipMessage {
  def parse(bytes: Array[Byte]): GossipMessage = {
    val firstByte = bytes(0)

    if (firstByte == 0.byteValue()) Unpickle.apply[SynGossipMessage].fromBytes(ByteBuffer.wrap(bytes.tail))
    else if (firstByte == 1.byteValue()) Unpickle.apply[SynAckGossipMessage].fromBytes(ByteBuffer.wrap(bytes.tail))
    else ???
  }
}