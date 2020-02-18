package swapnil.meta

import java.net.InetAddress
import java.util.Timer

import swapnil.meta.communication.{SimpleSocketServer, SocketClientFactory}
import swapnil.meta.gossip._
import swapnil.meta.gossip.handlers.{GossipHandler, SynAck2Handler, SynHandler}

class Node(seedNodes: Set[NodeIdentity], dataPort: Int, gossipPort: Int) {
  private val selfIdentity = NodeIdentity(InetAddress.getLocalHost.getHostAddress, dataPort, gossipPort)
  private val nodeState = new NodeState(selfIdentity)

  def identity: NodeIdentity = nodeState.selfIdentity

  def start(): Unit = {
    initGossip()
  }

  def stop(): Unit = ???

  private def initGossip(): Unit = {
    val messageProtocol = new MessageProtocol

    val synHandler = new SynHandler(nodeState, messageProtocol)
    val synAck2Handler = new SynAck2Handler(nodeState, messageProtocol)
    val gossipHandler = new GossipHandler(synHandler, synAck2Handler)
    val gossipServer = new SimpleSocketServer(gossipPort, gossipHandler)
    gossipServer.start()

    val socketClientFactory = new SocketClientFactory()
    val gossiper = new Gossiper(nodeState, socketClientFactory, messageProtocol)

    if(seedNodes.nonEmpty) gossiper.initGossipWith(seedNodes.head)
    new Timer().scheduleAtFixedRate(new RandomGossipTask(nodeState, gossiper), 0, 1000)
  }
}
