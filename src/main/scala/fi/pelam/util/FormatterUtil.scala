package fi.pelam.util

import java.text.{NumberFormat, ParsePosition}

object FormatterUtil {
  type Formatter[T] = (T) => String
  type Parser = (String) => Option[Number]

  /**
    * This function is a workaround for the Java feature of `NumberFormat`
    * not being thread safe. http://stackoverflow.com/a/1285353/1148030
    *
    * This transformation returns a thread safe `Formatter` based on the
    * `NumberFormat` passed in as parameter.
    */
  def toSynchronizedFormatter[T](numberFormat: NumberFormat): Formatter[T] = {

    // Clone to ensure instance is only used in this scope
    val clonedFormatter = numberFormat.clone().asInstanceOf[NumberFormat]

    val formatterFunction = { (input: T) =>
      clonedFormatter.synchronized {
        clonedFormatter.format(input)
      }
    }

    formatterFunction
  }

  /**
    * This function is a workaround for the Java feature of `NumberFormat`
    * not being thread safe. http://stackoverflow.com/a/1285353/1148030
    *
    * This transformation returns a thread safe `Parser` based on the
    * `NumberFormat` passed in as parameter.
    */
  def toSynchronizedParser(numberFormat: NumberFormat): Parser = {

    // Clone to ensure instance is only used in this scope
    val clonedFormatter = numberFormat.clone().asInstanceOf[NumberFormat]

    val parserFunction = { (input: String) =>

      val position = new ParsePosition(0)

      val number = clonedFormatter.synchronized {
        clonedFormatter.parse(input, position)
      }

      if (position.getIndex == input.length) {
        Some(number)
      } else {
        None
      }

    }

    parserFunction
  }

}
