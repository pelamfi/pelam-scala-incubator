package fi.pelam.javafxnodedb

import fi.pelam.nodedb._

trait NodeUseListener[K <: AnyRef, N <: NodeDbNode[K]] {
  def update(node: N): Unit

  def remove(node: N): Unit
}
