package fi.pelam.nodedb

/**
  * Trait for nodes stored in NodeDb.
  *
  * @tparam K key used to identify nodes stored in NodeDb
  */
trait NodeDbNode[K <: AnyRef] {

  var key: Option[K]

  // A node may turn out to be invalid. Invalid nodes
  // won't be triggered.
  def isValid: Boolean

}
