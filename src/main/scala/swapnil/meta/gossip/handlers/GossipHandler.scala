package swapnil.meta.gossip.handlers

import java.io.{DataInputStream, DataOutputStream}

import swapnil.meta.communication.RequestHandler


class GossipHandler(synHandler: SynHandler, synAck2Handler: SynAck2Handler) extends RequestHandler {

  override def handle(inputStream: DataInputStream, outputStream: DataOutputStream): Unit = {
    synHandler.handle(inputStream, outputStream)

    synAck2Handler.handle(inputStream)
  }
}
