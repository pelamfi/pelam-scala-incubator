package fi.pelam.javafxutil

import java.text.NumberFormat
import java.util.Locale
import javafx.geometry.Rectangle2D
import javafx.stage.{Screen, Window}

import fi.pelam.util.FormatterUtil._

import scala.collection.JavaConversions._

object Rectangle2DUtil {

  private val formatter = NumberFormat.getInstance(Locale.ROOT)
  formatter.setGroupingUsed(false)
  formatter.setMaximumFractionDigits(2)

  val doubleParser = toSynchronizedParser(formatter)

  val doubleFormatter = toSynchronizedFormatter[Double](formatter)

  def rectangleToString(rec: Rectangle2D): String = {
    List(rec.getMinX, rec.getMinY, rec.getWidth, rec.getHeight)
      .map(d => doubleFormatter(d))
      .reduce(_ + "," + _)
  }

  def rectangleFromString(s: String): Rectangle2D = {
    val doubles: Array[Double] =
      s.split(',').map(doubleParser(_).map(_.doubleValue()).getOrElse(0.0))
        .padTo(4, 0.0)

    new Rectangle2D(doubles(0), doubles(1), doubles(2), doubles(3))
  }

  def rectangleFromWindow(win: Window): Rectangle2D = {
    new Rectangle2D(win.getX, win.getY, win.getWidth, win.getHeight)
  }

  def rectangleToWindow(rec: Rectangle2D, win: Window): Unit = {
    win.setX(rec.getMinX)
    win.setY(rec.getMinY)
    win.setWidth(rec.getWidth)
    win.setHeight(rec.getHeight)
  }

  def moveTopLeftCornerInsideWithMargin(window: Rectangle2D, bounds: Rectangle2D): Rectangle2D = {
    val shrinked: Rectangle2D = shrinkBottomRight(bounds, 16)

    val clippedWin: Rectangle2D = moveTopLeftCornerInside(window, shrinked)
    clippedWin
  }

  def moveTopLeftCornerInside(window: Rectangle2D, screen: Rectangle2D): Rectangle2D = {
    val windowMinXP = if (window.getMinX < screen.getMinX) {
      screen.getMinX
    } else {
      window.getMinX
    }

    val windowMinYP = if (window.getMinY < screen.getMinY) {
      screen.getMinY
    } else {
      window.getMinY
    }

    val windowMinX = if (windowMinXP > screen.getMaxX) {
      screen.getMaxX
    } else {
      windowMinXP
    }

    val windowMinY = if (windowMinYP > screen.getMaxY) {
      screen.getMaxY
    } else {
      windowMinYP
    }

    new Rectangle2D(windowMinX, windowMinY, window.getWidth, window.getHeight)
  }

  def shrinkBottomRight(screenBounds: Rectangle2D, shrinkAmount: Int): Rectangle2D = {
    val minX = screenBounds.getMinX
    val minY = screenBounds.getMinY
    val maxX = Math.max(minX, screenBounds.getMaxX - shrinkAmount)
    val maxY = Math.max(minY, screenBounds.getMaxY - shrinkAmount)

    new Rectangle2D(minX, minY, maxX - minX, maxY - minY)
  }

  def clampPointInsideAScreenWithMargin(window: Rectangle2D): Rectangle2D = {
    val clippeds: List[Option[Rectangle2D]] = for (screen: Screen <- Screen.getScreens.toList) yield {
      if (screen.getBounds.intersects(window)) {
        val bounds: Rectangle2D = screen.getVisualBounds
        Some(moveTopLeftCornerInsideWithMargin(window, bounds))
      } else {
        None
      }
    }

    clippeds.flatten.headOption.getOrElse {
      moveTopLeftCornerInsideWithMargin(window, Screen.getPrimary.getVisualBounds)
    }
  }

}
