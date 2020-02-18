package swapnil.meta.communication

import java.io.{BufferedInputStream, DataInputStream, DataOutputStream}
import java.net.{InetSocketAddress, Socket}

class SimpleSocketClient {
  val socket = new Socket()

  def init(inetSocketAddress: InetSocketAddress): (DataInputStream, DataOutputStream) = {

    socket.connect(inetSocketAddress)

    val inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream))
    val outputStream = new DataOutputStream(socket.getOutputStream)
    (inputStream, outputStream)
  }

  def close(): Unit = socket.close()
}


class SocketClientFactory() {
  def newSocketClient = new SimpleSocketClient
}