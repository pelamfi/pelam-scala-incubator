package fi.pelam.util

object SeqUtil {

  def maxSegmentLength[A](in: Seq[A], from: Int)(predicate: A => Boolean): Int = {

    val it = in.iterator.drop(from)
    var max = 0
    var count = 0

    while (it.hasNext) {
      val isMatch = predicate(it.next())
      if (isMatch) {
        count += 1
      } else {
        count = 0
      }
      max = Math.max(count, max)
    }

    max
  }


}
