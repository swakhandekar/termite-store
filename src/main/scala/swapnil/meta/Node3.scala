package swapnil.meta

object Node3 extends App {
  val seedNode = NodeIdentity("10.131.22.7", 8061, 8060)

  val node3 = new Node(Set(seedNode), 8091, 8090)
  node3.start()
}
