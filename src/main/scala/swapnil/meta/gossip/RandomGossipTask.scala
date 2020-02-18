package swapnil.meta.gossip

import java.util.TimerTask

import swapnil.meta.{NodeIdentity, NodeState}

import scala.collection.mutable
import scala.util.Random

class RandomGossipTask(nodeState: NodeState, gossiper: Gossiper) extends TimerTask {

  override def run(): Unit = {
    val reachableNodes = nodeState.fetchReachableNodes

    if (reachableNodes.nonEmpty) {
      gossiper.gossipWith(chooseOneReachableNode(reachableNodes))

    } else println(s"INFO: [${nodeState.selfIdentity}] No other node available to start the gossip")
  }

  private def chooseOneReachableNode(reachableNodes: mutable.Set[NodeIdentity]): NodeIdentity = {
    val random = new Random()
    val i = {
      val r = random.nextInt()
      if (r < 0) -r else r
    }

    reachableNodes.toArray.apply(i % reachableNodes.size)
  }
}
