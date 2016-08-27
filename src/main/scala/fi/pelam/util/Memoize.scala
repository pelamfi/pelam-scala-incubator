package fi.pelam.util

import scala.collection.mutable

// Inspired by https://michid.wordpress.com/2009/02/23/function_mem/

class Memoize[-T, +R](innerFunc: T => R) extends (T => R) {

  private[this] val memoizeCache = mutable.Map.empty[T, R]

  def apply(parameter: T): R = {
    if (!memoizeCache.contains(parameter)) {
      val result = innerFunc(parameter)
      memoizeCache.put(parameter, result)
    }

    memoizeCache(parameter)
  }
}

object Memoize {
  def apply[T, R](f: T => R): (T => R) = new Memoize(f)
}
