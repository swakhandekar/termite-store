package swapnil.meta.gossip

import java.net.InetSocketAddress
import java.time.Instant
import java.util.Date

import org.mockito.Mockito.verify
import org.mockito.MockitoSugar
import org.mockito.captor.ArgCaptor
import org.scalatest.{Matchers, WordSpec}
import swapnil.meta.NodeState
import swapnil.meta.communication.TcpRequestSender

class GossipSynTest extends WordSpec with Matchers {

  import swapnil.meta.gossip.GossipSynTest._

  "GossipSyn" should {
    "Send a Syn GossipMessage with all the nodes it knows with their version" in {
      val tcpRequestSender = MockitoSugar.mock[TcpRequestSender]
      val gossipSyn = new GossipSyn(currentNodeState, tcpRequestSender)
      val toCaptor = ArgCaptor[InetSocketAddress]
      val messageCaptor = ArgCaptor[Array[Byte]]

      gossipSyn.run()

      verify(tcpRequestSender).send(toCaptor.capture, messageCaptor.capture)
      List(toCaptor.value) should contain oneOf(new InetSocketAddress(host1, 8090), new InetSocketAddress(host2, 8090))
      messageCaptor hasCaptured expectedMessage.deserialize
    }
  }
}

private object GossipSynTest {
  val host1 = "192.168.0.1"
  val host2 = "192.168.0.2"

  private val (knownNode1, version1) = (NodeIdentity(host1, 8080, 8090), Date.from(Instant.now()))
  private val (knownNode2, version2) = (NodeIdentity(host2, 8080, 8090), Date.from(Instant.now()))

  private val selfIdentity = NodeIdentity("10.131.4.220", 8080, 8090)

  val currentNodeState: NodeState = {
    val state = new NodeState(selfIdentity, Set())
    state.updateNodeInfo(knownNode1, version1)
    state.updateNodeInfo(knownNode2, version2)
    state
  }

  val expectedMessage: SynGossipMessage = {
    val knownHostsList = Set(
      NodeVersion(knownNode2, version2.toString),
      NodeVersion(knownNode1, version1.toString),
      NodeVersion(selfIdentity, currentNodeState.fetchVersion().toString)
    )

    SynGossipMessage(knownHostsList)
  }
}