package fi.pelam.util

import java.util.Locale

class EnumLocalizationMap[T](val map: Map[T, String], val reverseMap: Map[String, T]) {

  def getReverse(localized: String): Option[T] = {
    val key = localized.trim.toLowerCase(Locale.ROOT)
    reverseMap.get(key)
  }

  def get(enum: T): String = map(enum)
}
