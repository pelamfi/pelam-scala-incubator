package fi.pelam.util

object EitherUtil {

  // http://stackoverflow.com/a/36553598/1148030
  def partitionEithers[A, B](input: Traversable[Either[A, B]]): (IndexedSeq[A], IndexedSeq[B]) = {
    val a = IndexedSeq.newBuilder[A]
    a.sizeHint(input)
    val b = IndexedSeq.newBuilder[B]
    b.sizeHint(input)

    for (x <- input) {
      x match {
        case Left(aItem) => a += aItem
        case Right(bItem) => b += bItem
      }
    }

    (a.result(), b.result())
  }

  def pullUpEithers[A, B](input: Traversable[Either[A, B]]): Either[IndexedSeq[A], IndexedSeq[B]] = {
    val (aResult, bResult) = partitionEithers(input)
    if (aResult.isEmpty) {
      Right(bResult)
    } else {
      Left(aResult)
    }
  }
}
