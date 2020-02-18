package swapnil.meta.communication

import java.io.{DataInputStream, DataOutputStream}


trait RequestHandler {
  def handle(inputStream: DataInputStream, outputStream: DataOutputStream)
}
