package swapnil.meta

object Node1 extends App {
  val seedNode = NodeIdentity("10.131.22.7", 8061, 8060)

  val node1 = new Node(Set(seedNode), 8071, 8070)
  node1.start()
}
