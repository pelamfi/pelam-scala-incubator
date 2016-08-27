package fi.pelam.javafxutil

import javafx.geometry.Rectangle2D

import fi.pelam.javafxutil.Rectangle2DUtil._
import org.junit.Assert._
import org.junit.Test

class Rectangle2DUtilTest {

  @Test
  def testShrinkBottomRight(): Unit = {
    assertEquals("120,345,40,90",
      rectangleToString(
        shrinkBottomRight(new Rectangle2D(120, 345, 50, 100), 10)))
  }

  @Test
  def moveTopLeftCornerInsideWithMarginTest(): Unit = {
    val screen = new Rectangle2D(123, 345, 50, 100)

    assertEquals("157,429,50,100",
      rectangleToString(
        moveTopLeftCornerInsideWithMargin(new Rectangle2D(400, 430, 50, 100), screen)))
  }

  @Test
  def moveTopLeftCornerInsideWithMarginTest2(): Unit = {
    val screen = new Rectangle2D(123, 345, 50, 100)

    assertEquals("123,345,50,100",
      rectangleToString(
        moveTopLeftCornerInsideWithMargin(new Rectangle2D(120, 344, 50, 100), screen)))

  }

  @Test
  def rectangleToStringTest(): Unit = {
    assertEquals("123,345,50,100", rectangleToString(new Rectangle2D(123, 345, 50, 100)))
  }

  @Test
  def rectangleToStringTest2(): Unit = {
    assertEquals("123000,345,50,1000000", rectangleToString(new Rectangle2D(123000, 345, 50, 1000000)))
  }

  @Test
  def rectangleFromStringTest(): Unit = {
    assertEquals(new Rectangle2D(123, 345, 50, 100), rectangleFromString("123,345,50,100"))
  }
}