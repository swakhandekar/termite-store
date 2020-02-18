package swapnil.meta.gossip

import java.io.{DataInputStream, DataOutputStream}

class MessageProtocol {
  def readMessage(inputStream: DataInputStream): Array[Byte] = {
    val dataLength = inputStream.readInt()
    inputStream.readNBytes(dataLength)
  }

  def writeMessage(outputStream: DataOutputStream, data: Array[Byte]): Unit = {
    val length: Int = data.length
    outputStream.writeInt(length)
    outputStream.write(data)
  }
}
