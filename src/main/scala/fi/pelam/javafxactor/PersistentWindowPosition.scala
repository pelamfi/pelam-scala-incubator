package fi.pelam.javafxactor

import java.util.prefs.Preferences
import javafx.scene.Scene
import javafx.stage.Stage

import fi.pelam.actorutil.Throttle
import fi.pelam.javafxutil.JavaFxImplicits._
import fi.pelam.javafxutil.Rectangle2DUtil._
import fi.pelam.util.PrefsUtil._
import grizzled.slf4j.Logging

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._

object PersistentWindowPosition extends Logging {

  def apply(stage: Stage, preferences: Preferences)(
    implicit scheduler: akka.actor.Scheduler, executor: ExecutionContextExecutor): Unit = {

    val persistWindowDimensions = Throttle(delay = 100.milliseconds) {
      val scene = stage.getScene
      if (scene != null) {
        val windowString = rectangleToString(rectangleFromWindow(scene.getWindow))
        info(s"Persist window pos $windowString ${preferences.absolutePath()}")
        preferences.put("windowRectangle", windowString)
        preferences.flush()
      }
    }

    val windowResizeListener = javaFxChangeListener1Double { (value: Double) =>
      persistWindowDimensions.apply(())
    }

    stage.sceneProperty().addListener { (oldScene: Scene, newScene: Scene) =>
      for (scene <- Option(oldScene); win <- Option(scene.getWindow)) {
        win.widthProperty().removeListener(windowResizeListener)
        win.heightProperty().removeListener(windowResizeListener)
        win.xProperty().removeListener(windowResizeListener)
        win.yProperty().removeListener(windowResizeListener)
      }

      for (scene <- Option(newScene); win <- Option(scene.getWindow)) {
        // http://stackoverflow.com/a/26204372/1148030

        ifDefined(preferences, "windowRectangle") { rectangleString =>
          val rectangle = clampPointInsideAScreenWithMargin(rectangleFromString(rectangleString))
          info(s"Restore window pos $rectangleString clamped ${rectangleToString(rectangle)} ${preferences.absolutePath()}")
          rectangleToWindow(rectangle, win)
        }

        win.widthProperty().addListener(windowResizeListener)
        win.heightProperty().addListener(windowResizeListener)
        win.xProperty().addListener(windowResizeListener)
        win.yProperty().addListener(windowResizeListener)
      }
    }
  }
}