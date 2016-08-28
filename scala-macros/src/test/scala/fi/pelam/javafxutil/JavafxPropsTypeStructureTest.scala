package fi.pelam.javafxutil

import org.junit.Test

trait Props[I] {
  def toScala: I
}

trait FromScalaToProps[I, +P <: Props[I]] extends Function[I, P] {
  override def apply(i: I): P
}

case class Immutable(i: Int)

class PropsSub extends Props[Immutable] {
  override def toScala: Immutable = Immutable(523)
}

object PropsSub extends FromScalaToProps[Immutable, PropsSub] {
  override def apply(i: Immutable): PropsSub = new PropsSub()
}

/**
  * Test to sketch out the types used in JavafxPropsMaker, because doing
  * this while editing the macro code at the same time would be too much.
  * The error messages get confusing when there is a macro involved.
  */
class JavafxPropsTypeStructureTest {

  @Test
  def test(): Unit = {
    val toImmutableResult: Immutable = new PropsSub().toScala
    val toPropsResult: PropsSub = PropsSub(Immutable(1))

    val mangledProps: Props[Immutable] = new PropsSub()

    val mangledPropsMaker: FromScalaToProps[Immutable, Props[Immutable]] = PropsSub

    val propsFromMangled: Props[Immutable] = mangledPropsMaker(Immutable(2))
    val immutableFromMangled2: Immutable = propsFromMangled.toScala
    val propsFomMangled2: Props[Immutable] = mangledPropsMaker(immutableFromMangled2)
  }

}
