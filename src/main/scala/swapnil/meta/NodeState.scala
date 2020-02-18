package swapnil.meta

import java.time.Instant
import java.util.Date

import swapnil.meta.gossip.NodeVersion
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

  def fetchVersionsOf(nodes: Set[NodeIdentity]): Set[NodeVersion] = {
    nodes.map(ni => {
      if (ni == selfIdentity)
        NodeVersion(selfIdentity, fetchVersion().toFormattedDateString)
      else
        NodeVersion(ni, nodeVsVersionMap(ni).toFormattedDateString)
    })
  }

  def updateNodeInfo(nodeVersion: NodeVersion): Unit = {
    updateReachable(nodeVersion.identity)
    nodeVsVersionMap.update(nodeVersion.identity, nodeVersion.version.toDate)

    updateVersion()
  }

  private def updateVersion(): Unit = version = getCurrentTime

  private def getCurrentTime: Date = {
    Date.from(Instant.now())
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
