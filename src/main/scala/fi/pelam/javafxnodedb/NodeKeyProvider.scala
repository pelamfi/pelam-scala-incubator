package fi.pelam.javafxnodedb

trait NodeKeyProvider[K <: AnyRef] {
  val nodeKey: K
}
