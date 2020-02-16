package swapnil.meta.communication

import java.net.InetSocketAddress

import scala.util.{Failure, Success}

class TcpRequestSender {
  def send(to: InetSocketAddress, message: Array[Byte]): Either[Success[Boolean], Failure[String]] = ???
}
