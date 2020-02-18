package swapnil.meta.communication

import java.io.{BufferedInputStream, DataInputStream, DataOutputStream}
import java.net.ServerSocket

class SimpleSocketServer(port: Int, requestHandler: RequestHandler) extends Thread {
  override def run(): Unit = {
    val serverSocket = new ServerSocket(port, 10)

    while (true) {
      val socket = serverSocket.accept()

      val outputStream = new DataOutputStream(socket.getOutputStream)
      val inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream))

      requestHandler.handle(inputStream, outputStream)

      inputStream.close()
      outputStream.close()
      socket.close()
    }
  }
}