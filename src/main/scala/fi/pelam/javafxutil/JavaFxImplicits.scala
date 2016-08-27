package fi.pelam.javafxutil

import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.event.EventHandler
import javafx.util.Callback

/**
  * Helper to make using JavaFX event handlers from Scala nicer.
  * Scala does not support Java style lambdas yet.
  *
  * Idea from here:
  * http://stackoverflow.com/a/27027035/1148030
  */
//noinspection LanguageFeature
object JavaFxImplicits {

  implicit def javaFxEventHandler[E <: javafx.event.Event](eventFunc: (E) => Any): EventHandler[E] = {
    new EventHandler[E] {
      override def handle(eventObj: E): Unit = eventFunc(eventObj)
    }
  }

  implicit def javaFxChangeListenerDouble(eventFunc: (ObservableValue[_ <: java.lang.Number], Double, Double) => Any): ChangeListener[java.lang.Number] = {
    new ChangeListener[java.lang.Number]() {
      override def changed(observable: ObservableValue[_ <: java.lang.Number], oldValue: java.lang.Number, newValue: java.lang.Number): Unit = eventFunc(observable, oldValue.doubleValue(), newValue.doubleValue())
    }
  }

  implicit def javaFxChangeListener1Double(eventFunc: (Double) => Any): ChangeListener[java.lang.Number] = {
    new ChangeListener[java.lang.Number]() {
      override def changed(observable: ObservableValue[_ <: java.lang.Number], oldValue: java.lang.Number, newValue: java.lang.Number): Unit = eventFunc(newValue.doubleValue())
    }
  }

  implicit def javaFxChangeListener1Boolean(eventFunc: (Boolean) => Any): ChangeListener[java.lang.Boolean] = {
    new ChangeListener[java.lang.Boolean]() {
      override def changed(observable: ObservableValue[_ <: java.lang.Boolean], oldValue: java.lang.Boolean, newValue: java.lang.Boolean): Unit = eventFunc(newValue)
    }
  }

  implicit def javaFxChangeListener1Any[T <: AnyRef](eventFunc: (T) => Any): ChangeListener[T] = {
    new ChangeListener[T]() {
      override def changed(observable: ObservableValue[_ <: T], oldValue: T, newValue: T): Unit = eventFunc(newValue)
    }
  }

  implicit def javaFxChangeListener2Any[T <: AnyRef](eventFunc: (T, T) => Any): ChangeListener[T] = {
    new ChangeListener[T]() {
      override def changed(observable: ObservableValue[_ <: T], oldValue: T, newValue: T): Unit = eventFunc(oldValue, newValue)
    }
  }

  implicit def javaFxChangeListener2Double(eventFunc: (Double, Double) => Any): ChangeListener[java.lang.Number] = {
    new ChangeListener[java.lang.Number]() {
      override def changed(observable: ObservableValue[_ <: java.lang.Number], oldValue: java.lang.Number, newValue: java.lang.Number): Unit = eventFunc(oldValue.doubleValue(), newValue.doubleValue())
    }
  }

  implicit def javaFxCallback[P, R](func: (P) => R): Callback[P, R] = {
    new Callback[P, R] {
      override def call(param: P): R = func(param)
    }
  }
}
