package swapnil.meta.gossip

import java.net.InetSocketAddress
import java.util.TimerTask

import swapnil.meta.NodeState
import swapnil.meta.communication.TcpRequestSender

import scala.collection.mutable
import scala.util.Random

class GossipSyn(nodeState: NodeState, tcpRequestSender: TcpRequestSender) extends TimerTask {
  private def synWith(node: NodeIdentity): Unit = {
    val selfVersion = NodeVersion(nodeState.nodeIdentity, nodeState.fetchVersion().toString)
    val nodeVersions = (nodeState.fetchAllAvailableVersions() :+ selfVersion).toSet

    val message = SynGossipMessage(nodeVersions)
    val nodeGossipAddress = new InetSocketAddress(node.inetAddress, node.gossipPort)
    tcpRequestSender.send(nodeGossipAddress, message.deserialize())
  }

  private def chooseOneReachableNode(reachableNodes: mutable.Set[NodeIdentity]) = {
    val random = new Random()
    val i = {
      val r = random.nextInt()
      if(r < 0) -r else r
    }

    reachableNodes.toArray.apply(i % reachableNodes.size)
  }

  override def run(): Unit = {
    val nodeToGossipWith = chooseOneReachableNode(nodeState.fetchReachableNodes)
    synWith(nodeToGossipWith)
  }
}
