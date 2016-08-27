package fi.pelam.javafxnodedb

import fi.pelam.nodedb._

trait NodeDbJavaFxNode[K <: AnyRef] extends NodeDbNode[K] {

  // Nodes
  override def isValid = node.getParent != null

  val node: javafx.scene.Node

}
