package fi.pelam.util

import java.util.{Locale, ResourceBundle}

import com.google.common.cache.{CacheBuilder, CacheLoader}
import enumeratum.{Enum, EnumEntry}

class EnumLocalizationMapFactory(bundleLoader: (Locale) => ResourceBundle) {

  private[this] val enumMapCache =
    CacheBuilder.newBuilder()
      .maximumSize(20)
      .build(new CacheLoader[(Enum[_], Locale), EnumLocalizationMap[_]] {
        override def load(key: (Enum[_], Locale)): EnumLocalizationMap[_] = {
          val enum = key._1
          val locale = key._2

          // http://stackoverflow.com/a/12995388/1148030
          val prefix = enum.getClass.getSimpleName.split("\\$").last

          val reverseBuilder = Map.newBuilder[String, Any]

          val builder = Map.newBuilder[Any, String]

          val bundle = bundleLoader(locale)
          for (keyValue <- enum.namesToValuesMap) {
            val localized = bundle.getString(prefix + keyValue._1)
            val normalized = localized.trim.toLowerCase(Locale.ROOT)
            val enumValue = keyValue._2
            reverseBuilder += normalized -> enumValue
            builder += enumValue -> localized
          }

          new EnumLocalizationMap(builder.result, reverseBuilder.result)

        }
      })

  def get[T <: EnumEntry](locale: java.util.Locale, enum: Enum[T]): EnumLocalizationMap[T] = {

    val enumWithoutType: Enum[_ <: EnumEntry] = enum.asInstanceOf[Enum[_ <: EnumEntry]]
    val key: (Enum[_ <: EnumEntry], java.util.Locale) = (enumWithoutType, locale)
    val untypedMap = enumMapCache.get(key)
    untypedMap.asInstanceOf[EnumLocalizationMap[T]]
  }
}
