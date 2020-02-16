package swapnil.meta

import java.util.{Calendar, Date, TimeZone}

import swapnil.meta.gossip.{NodeIdentity, NodeVersion}

import scala.collection.mutable

class NodeState(val nodeIdentity: NodeIdentity, seedNodes: Set[NodeIdentity]) {
  private var version = getCurrentTime

  private val reachableNodes: mutable.Set[NodeIdentity] = {
    val set = mutable.Set[NodeIdentity]()
    seedNodes.foreach(n => set.add(n))
    set
  }

  private val unReachableNodes: mutable.Set[NodeIdentity] = mutable.Set()
  private val nodeVsVersionMap: mutable.Map[NodeIdentity, Date] = mutable.Map()

  def fetchReachableNodes: mutable.Set[NodeIdentity] = reachableNodes

  def fetchUnReachableNodes: mutable.Set[NodeIdentity] = unReachableNodes

  def fetchVersion(): Date = version

  def fetchAllAvailableVersions(): List[NodeVersion] = {
    nodeVsVersionMap.toList.map(el => NodeVersion(el._1, el._2.toString))
  }

  def updateNodeInfo(nodeIdentity: NodeIdentity, version: Date): Unit = {
    updateReachable(nodeIdentity)
    nodeVsVersionMap.update(nodeIdentity, version)

    updateVersion()
  }

  private def updateVersion(): Unit = version = getCurrentTime

  private def getCurrentTime = {
    Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTime
  }

  private def updateReachable(nodeIdentity: NodeIdentity): Unit = {
    reachableNodes.add(nodeIdentity)
    unReachableNodes.remove(nodeIdentity)
  }

  def updateUnreachable(nodeIdentity: NodeIdentity): Boolean = {
    unReachableNodes.add(nodeIdentity)
    reachableNodes.remove(nodeIdentity)
  }
}


/*
ip
heartbeat stage -> time stamp
health
*/