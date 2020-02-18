package swapnil.meta

import java.net.InetAddress
import java.util.Timer

import swapnil.meta.communication.{SimpleSocketClient, SimpleSocketServer, SocketClientFactory}
import swapnil.meta.gossip._

class Node(seedNodes: Set[NodeIdentity], dataPort: Int, gossipPort: Int) {
  private val selfIdentity = NodeIdentity(InetAddress.getLocalHost.getHostAddress, dataPort, gossipPort)
  private val nodeState = new NodeState(selfIdentity)

  def identity: NodeIdentity = nodeState.selfIdentity

  def start(): Unit = {
    initGossip()
  }

  def stop(): Unit = ???

  private def initGossip(): Unit = {
    val tcpRequestSender = new SimpleSocketClient()
    val messageProtocol = new MessageProtocol
    val gossipHandler = new GossipHandler(nodeState, tcpRequestSender, messageProtocol)
    val gossipServer = new SimpleSocketServer(gossipPort, gossipHandler)
    val socketClientFactory = new SocketClientFactory()
    val gossiper = new Gossiper(nodeState, socketClientFactory, messageProtocol)

    gossipServer.start()
    if(seedNodes.nonEmpty) gossiper.initGossipWith(seedNodes.head)
    new Timer().scheduleAtFixedRate(new RandomGossipTask(nodeState, gossiper), 0, 1000)
  }
}
