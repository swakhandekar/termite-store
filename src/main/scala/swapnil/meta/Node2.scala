package swapnil.meta

object Node2 extends App {
  val seedNode = NodeIdentity("10.131.22.7", 8061, 8060)

  val node2 = new Node(Set(seedNode), 8081, 8080)
  node2.start()
}
