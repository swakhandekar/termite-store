package swapnil.meta.gossip

import java.nio.ByteBuffer

import boopickle.Default._

sealed trait GossipMessageType

case object Syn extends GossipMessageType

case object SynAck extends GossipMessageType

case object SynAck2 extends GossipMessageType

sealed trait GossipMessage

case class NodeVersion(ni: NodeIdentity, version: String)

case class SynGossipMessage(knownNodeVersions: Set[NodeVersion]) extends GossipMessage

case class TransferableGossipMessage(messageType: GossipMessageType, message: GossipMessage) {
  def deserialize: Array[Byte] = {
    val encodedMessageType = TransferableGossipMessage.getTypeOf(messageType)
    val messageTypeByte = encodedMessageType

    val buffer = messageType match {
      case Syn => Pickle.intoBytes(message.asInstanceOf[SynGossipMessage])
      case SynAck => ???
      case SynAck2 => ???
    }

    messageTypeByte +: buffer.array()
  }
}

object TransferableGossipMessage {
  def parse(bytes: Array[Byte]): GossipMessage = {
    val messageType = getTypeFromByte(bytes(0))
    messageType match {
      case Syn => Unpickle.apply[SynGossipMessage].fromBytes(ByteBuffer.wrap(bytes.tail))
      case SynAck => ???
      case SynAck2 => ???
    }
  }

  def getTypeOf(messageType: GossipMessageType): Byte = {
    messageType match {
      case Syn => 0.byteValue()
      case SynAck => 1.byteValue()
      case SynAck2 => 2.byteValue()
    }
  }

  def getTypeFromByte(byte: Byte): GossipMessageType = {
    if (byte == 0.byteValue()) Syn
    else if (byte == 1.byteValue()) SynAck
    else SynAck2
  }
}