package swapnil.meta

import java.time.Instant
import java.util.Date

import swapnil.meta.utils.RichUtils._

import scala.collection.mutable

class NodeState(val selfIdentity: NodeIdentity) {
  private var version = getCurrentTime

  private val reachableNodes: mutable.Set[NodeIdentity] = mutable.Set[NodeIdentity]()

  private val unReachableNodes: mutable.Set[NodeIdentity] = mutable.Set()
  private val nodeVsVersionMap: mutable.Map[NodeIdentity, Date] = mutable.Map()

  def fetchReachableNodes: mutable.Set[NodeIdentity] = reachableNodes

  def fetchUnReachableNodes: mutable.Set[NodeIdentity] = unReachableNodes

  def fetchVersion(): Date = version

  def fetchAllAvailableVersions(): Map[NodeIdentity, Date] = nodeVsVersionMap.toMap

  def fetchVersionsOf(nodes: Set[NodeIdentity]): Map[NodeIdentity, String] = {
    nodes.map(ni => {
      if (ni == selfIdentity)
        (selfIdentity, fetchVersion().toFormattedDateString)
      else
        (ni, nodeVsVersionMap(ni).toFormattedDateString)
    }).toMap
  }

  def updateNodeInfo(node: NodeIdentity, version: Date): Unit = {
    updateReachable(node)
    nodeVsVersionMap.update(node, version)

    updateVersion()
  }

  private def updateVersion(): Unit = version = getCurrentTime

  private def getCurrentTime: Date = Date.from(Instant.now())

  private def updateReachable(nodeIdentity: NodeIdentity): Unit = {
    reachableNodes.add(nodeIdentity)
    unReachableNodes.remove(nodeIdentity)
  }

  def updateUnreachable(nodeIdentity: NodeIdentity): Boolean = {
    unReachableNodes.add(nodeIdentity)
    reachableNodes.remove(nodeIdentity)
  }
}
