package fi.pelam.javafxactor

import javafx.util.Callback

import akka.actor.Actor

import scala.collection.mutable

class ControllerFactory extends Callback[Class[_], AnyRef] {

  val controllerMap = mutable.Map[Class[_], Actor]()

  def register(controller: Actor): Unit = {
    controllerMap(controller.getClass) = controller
  }

  override def call(param: Class[_]): AnyRef = controllerMap(param)

}
