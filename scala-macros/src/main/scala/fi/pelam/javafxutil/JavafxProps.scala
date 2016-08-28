package fi.pelam.javafxutil

import javafx.beans.property._

import scala.language.experimental.macros

/**
  * A base trait for generated JavaFX compatible beans.
  *
  * Also provides some handy metadata services.
  *
  * @tparam I case class to be mapped into a JavaFx compatible bean.
  */
trait JavafxProps[I] {

  def toScala: I

  def setFromScala(scalaObj: I): Unit

  val properties: IndexedSeq[Property[_]]
  val numberProperties: IndexedSeq[Property[Number]]

  def getProperty(name: String): Property[_] = {
    properties.collectFirst {
      case a if a.getName.equals(name) => a
    }.getOrElse {
      sys.error(s"Property with name $name not found among property names $propertyNames.")
    }
  }

  def getNumberProperty(name: String): Property[Number] = {
    numberProperties.collectFirst {
      case a if a.getName.equals(name) => a
    }.getOrElse {
      sys.error(s"Number property with name $name not found among property names $propertyNames.")
    }
  }

  def propertyNames = properties.map(_.getName())
}
