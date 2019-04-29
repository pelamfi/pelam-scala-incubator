package fi.pelam.util

import fi.pelam.util.TopologicalSort._
import org.junit.Assert._
import org.junit.Test

import scala.collection.mutable

class TopologicalSortTest {

  trait BaseNode {
    val referenced: Seq[BaseNode]
  }

  case class Node(name: String)(override val referenced: BaseNode*) extends BaseNode

  case class MutableNode(name: String)(override val referenced: mutable.Buffer[BaseNode] = mutable.Buffer()) extends BaseNode

  def visitor(start: BaseNode, callback: (BaseNode) => Unit): Unit = {
    start.referenced.foreach(callback)
  }

  val a = Node("a")()
  val b = Node("b")(a)
  val c = Node("c")(a)
  val d = Node("d")(b, c)
  val e = Node("e")(d)
  val f = Node("f")()

  @Test
  def sortInput(): Unit = {
    val Result(result, loops) = topoSort(List(d, c, b, f, a, e), visitor)

    assertEquals(IndexedSeq(a, b, c, d, f, e), result)
    assertEquals(IndexedSeq(), loops)
  }

  @Test
  def sortInput2(): Unit = {
    val Result(result, loops) = topoSort(List(d, c, b, a, f, e), visitor)

    assertEquals(IndexedSeq(a, b, c, d, f, e), result)
    assertEquals(IndexedSeq(), loops)
  }

  @Test
  def sortInput3(): Unit = {
    val Result(result, loops) = topoSort(List(e, d, c, b, a, f), visitor)

    assertEquals(IndexedSeq(a, b, c, d, e, f), result)
    assertEquals(IndexedSeq(), loops)
  }

  @Test
  def sortInput4(): Unit = {
    val Result(result, loops) = topoSort(List(f, e, d, c, b, a), visitor)

    assertEquals(IndexedSeq(f, a, b, c, d, e), result)
    assertEquals(IndexedSeq(), loops)
  }

  @Test
  def sortInputStable(): Unit = {
    val Result(result, loops) = topoSort(List(f, a, b, c, d, e), visitor)

    assertEquals(IndexedSeq(f, a, b, c, d, e), result)
    assertEquals(IndexedSeq(), loops)
  }

  @Test
  def sortInputStable2(): Unit = {
    val Result(result, loops) = topoSort(List(a, b, f, c, d, e), visitor)

    assertEquals(IndexedSeq(a, b, f, c, d, e), result)
    assertEquals(IndexedSeq(), loops)
  }

  @Test
  def sortWithLoops(): Unit = {
    val loop1 = MutableNode("loop1")(mutable.Buffer())
    val loop2 = MutableNode("loop2")(mutable.Buffer(loop1))
    val loop3 = MutableNode("loop3")(mutable.Buffer(loop2))
    loop1.referenced += loop3

    val Result(result, loops) = topoSort(List(d, c, loop3, b, loop1, f, loop2, a, e), visitor)

    assertEquals(IndexedSeq(IndexedSeq(loop3, loop1, loop2)), loops.toIndexedSeq)
    assertEquals(IndexedSeq(a, b, c, d, loop3, loop1, loop2, f, e), result.toIndexedSeq)
  }

  @Test
  def sortWithLoopsStable(): Unit = {
    val loopa = MutableNode("loopa")(mutable.Buffer())
    val loopb = MutableNode("loopb")(mutable.Buffer(loopa))
    val loopc = MutableNode("loopc")(mutable.Buffer(loopb))
    loopa.referenced += loopc

    val Result(result, loops) = topoSort(IndexedSeq(a, b, c, d, loopa, loopb, loopc, f, e), visitor)

    assertEquals(IndexedSeq(a, b, c, d, loopa, loopb, loopc, f, e), result.toIndexedSeq)
    assertEquals(IndexedSeq(IndexedSeq(loopa, loopb, loopc)), loops.toIndexedSeq)
  }

  @Test
  def sortWithLoopsStable2(): Unit = {
    val loop1 = MutableNode("loop1")(mutable.Buffer())
    val loop2 = MutableNode("loop2")(mutable.Buffer(loop1))
    val loop3 = MutableNode("loop3")(mutable.Buffer(loop2))
    loop1.referenced += loop3

    val Result(result, loops) = topoSort(IndexedSeq(a, b, c, d, loop3, loop1, loop2, f, e), visitor)

    assertEquals(IndexedSeq(a, b, c, d, loop3, loop1, loop2, f, e), result.toIndexedSeq)
    assertEquals(IndexedSeq(IndexedSeq(loop3, loop1, loop2)), loops.toIndexedSeq)
  }

  @Test
  def sortWithLoopsAndNodesOnLoops(): Unit = {
    val loop1 = MutableNode("loop1")(mutable.Buffer())
    val loop2 = MutableNode("loop2")(mutable.Buffer(loop1))
    val loop3 = MutableNode("loop3")(mutable.Buffer(loop2))
    loop1.referenced += loop3

    val loop2child1 = Node("loop2child1")(loop2)
    val loop2child2 = Node("loop2child2")(loop2)
    val loop2child2child = Node("loop2child2child")(loop2child2)

    val Result(result, loops) = topoSort(IndexedSeq(loop2child1, loop2child2child, loop2child2,
      a, b, c, d, loop3, loop1, loop2, f, e), visitor)

    assertEquals(IndexedSeq(loop2, loop3, loop1,
      loop2child1, loop2child2, loop2child2child,
      a, b, c, d, f, e), result.toIndexedSeq)

    assertEquals(IndexedSeq(IndexedSeq(loop2, loop3, loop1)), loops.toIndexedSeq)
  }

  @Test
  def sortWithLoopsAndNodesOnLoopsStable(): Unit = {
    val loop1 = MutableNode("loop1")(mutable.Buffer())
    val loop2 = MutableNode("loop2")(mutable.Buffer(loop1))
    val loop3 = MutableNode("loop3")(mutable.Buffer(loop2))
    loop1.referenced += loop3

    val loop2child1 = Node("loop2child1")(loop2)
    val loop2child2 = Node("loop2child2")(loop2)
    val loop2child2child = Node("loop2child2child")(loop2child2)

    val Result(result, loops) = topoSort(IndexedSeq(loop2, loop3, loop1,
      loop2child1, loop2child2, loop2child2child,
      a, b, c, d, f, e), visitor)

    assertEquals(IndexedSeq(loop2, loop3, loop1,
      loop2child1, loop2child2, loop2child2child,
      a, b, c, d, f, e), result.toIndexedSeq)

    assertEquals(IndexedSeq(IndexedSeq(loop2, loop3, loop1)), loops.toIndexedSeq)
  }
}