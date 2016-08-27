package fi.pelam.nodedb

import fi.pelam.javafxnodedb.NodeUseListener
import grizzled.slf4j.Logging

import scala.collection.mutable

/**
  * Tracks a set of nodes and possibly associated listeners interested in them.
  *
  * Waits for nodes identified by keys to be and and
  * when matching nodes (and keys) are seen, calls the listeners interested
  * in those keys.
  *
  * Original use case involved tracking JavaFX nodes in a JavaFX widget
  * and triggering listeners when needed.
  *
  * The original use case for this is to draw markers on top of specific cells in JavaFX table widget.
  * I could not find any more straight forward way to acchieve that goal. The reuse of cells in the
  * table made everything tricky.
  *
  * @tparam K key that is used to identify these particular nodes in the NodeDb.
  *           Key could for example identify logical position in a table view.
  *           In that case, it could just contain number for row and column, but more likely they could
  *           be pointers to business objects.
  */
abstract class NodeDb[K <: AnyRef, N <: NodeDbNode[K]] extends Logging {

  private[this] val listenerDb = mutable.HashMap[K, mutable.ArrayBuffer[NodeSetListener]]()

  private[this] val nodeDb = mutable.HashMap[K, N]()

  val nodeUseListener = new NodeUseListener[K, N] {
    override def update(node: N): Unit = NodeDb.this.update(node)

    override def remove(node: N): Unit = NodeDb.this.remove(node)
  }

  def findNode(key: K): Option[N] = nodeDb.get(key)

  def register(entry: NodeSetListener) = {
    for (key <- entry.nodeKeys) {
      if (!listenerDb.contains(key)) {
        listenerDb(key) = mutable.ArrayBuffer[NodeSetListener]()
      }

      listenerDb(key) += entry

      maybeTriggerEntry(entry)
    }
  }

  def update(dbNode: N) = {
    for (key <- dbNode.key) {
      nodeDb(key) = dbNode

      for (entries <- listenerDb.get(key);
           entry <- entries) {

        maybeTriggerEntry(entry)
      }
    }
  }

  def remove(dbNode: N) = {
    for (key <- dbNode.key) {
      if (nodeDb.contains(key)) {

        for (entries <- listenerDb.get(key);
             entry <- entries) {

          entry.clearListener()
        }

        nodeDb.remove(key)

      }
    }
  }

  def clearListeners() = {
    for (entries <- listenerDb.values; entry <- entries) {
      entry.clearListener()
    }

    listenerDb.clear()
  }

  def maybeTriggerEntry(entry: NodeSetListener) = {
    val nodeKeys = entry.nodeKeys.map(nodeDb.get(_).filter(_.isValid))

    if (nodeKeys.count(_.isDefined) == nodeKeys.size) {
      entry.maybeTrigger(nodeKeys.flatten)
    }
  }

  def triggerAllEntries() = {
    for (entries <- listenerDb.values; entry <- entries) {
      maybeTriggerEntry(entry)
    }
  }

  def clear() = {
    clearListeners()

    nodeDb.clear()
  }

  /**
    * Waits for nodes to be added to the db and
    * when required nodes (keys) are seen, calls the trigger
    */
  abstract class NodeSetListener() {
    def nodeKeys: IndexedSeq[K]

    private[this] var triggered = false

    final def maybeTrigger(dbNodes: IndexedSeq[N]) = {
      if (!triggered && dbNodes.count(dbNode => dbNode == null || !dbNode.isValid) == 0) {
        // All nodes specified by listener known, trigger the listener (which results in some rendering most likely)
        triggered = true
        trigger(dbNodes)
      }
    }

    def trigger(dbNodes: IndexedSeq[N])

    def unTrigger()

    final def clearListener() = {
      triggered = false
      unTrigger()
    }
  }

}
