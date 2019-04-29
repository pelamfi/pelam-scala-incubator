package fi.pelam.util

import scala.collection.mutable

// Based on https://en.wikipedia.org/wiki/Topological_sorting?oldformat=true#Depth-first_search
object TopologicalSort {

  case class Result[T](result: IndexedSeq[T], loops: IndexedSeq[IndexedSeq[T]])

  type Visit[T] = (T) => Unit

  // A visitor is a function that takes a node and a callback.
  // The visitor calls the callback for each node referenced by the given node.
  type Visitor[T] = (T, Visit[T]) => Unit

  def topoSort[T <: AnyRef](input: Iterable[T], visitor: Visitor[T]): Result[T] = {

    // Buffer, because it is operated in a stack like fashion
    val temporarilyMarked = mutable.Buffer[T]()

    val permanentlyMarked = mutable.HashSet[T]()

    val loopsBuilder = IndexedSeq.newBuilder[IndexedSeq[T]]

    val resultBuilder = IndexedSeq.newBuilder[T]

    def visit(node: T): Unit = {
      if (temporarilyMarked.contains(node)) {
        val loopStartIndex = temporarilyMarked.indexOf(node)
        val loop = node +: temporarilyMarked.slice(loopStartIndex + 1, temporarilyMarked.size).reverse.toIndexedSeq
        // Add the cycle separately so it is in one place and its order remains stable
        // when this algorithm is run multiple times.
        resultBuilder ++= loop
        permanentlyMarked ++= loop
        loopsBuilder += loop
      } else if (!permanentlyMarked.contains(node)) {

        temporarilyMarked += node

        visitor(node, visit)

        temporarilyMarked.remove(temporarilyMarked.size - 1, 1)

        if (!permanentlyMarked.contains(node)) {
          // Extra check due to cycle handling above.
          permanentlyMarked += node
          resultBuilder += node
        }
      }
    }

    for (i <- input) {
      if (!permanentlyMarked.contains(i)) {
        visit(i)
      }
    }

    Result(resultBuilder.result(), loopsBuilder.result())
  }
}
