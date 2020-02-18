package swapnil.meta.gossip

import java.util.Date

import org.mockito.captor.ArgCaptor
import org.mockito.{Mockito, MockitoSugar}
import org.scalatest.{Matchers, WordSpec}
import swapnil.meta.{NodeIdentity, NodeState}

class RandomGossipTaskTest extends WordSpec with Matchers {

  import RandomGossipTaskTest._

  "RandomGossipTask" should {
    "initiate a gossip session with one of the reachable nodes" in {
      val gossiper = MockitoSugar.mock[Gossiper]
      val randomGossipTask = new RandomGossipTask(nodeState, gossiper)
      val nodeCaptor = ArgCaptor[NodeIdentity]

      randomGossipTask.run()

      Mockito.verify(gossiper).initGossipWith(nodeCaptor.capture)
      List(nodeCaptor.value) should contain oneOf (knownNode1, knownNode2)
    }
  }
}

object RandomGossipTaskTest {
  val knownNode1: NodeIdentity = NodeIdentity("localhost", 7863, 5644)
  val knownNode2: NodeIdentity = NodeIdentity("localhost", 5678, 3409)

  val nodeState: NodeState = {
    val selfIdentity = NodeIdentity("localhost", 8080, 8090)
    val nodeState = new NodeState(selfIdentity)

    val version1 = new Date()
    val version2 = new Date()
    nodeState.updateNodeInfo(knownNode1, version1)
    nodeState.updateNodeInfo(knownNode2, version2)
    nodeState
  }
}