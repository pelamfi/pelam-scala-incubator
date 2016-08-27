package fi.pelam.javafxutil

import javafx.geometry.{BoundingBox, Bounds, Point2D}
import javafx.scene.Node
import javafx.scene.transform.Transform

import grizzled.slf4j.Logging

object TransformUtil extends Logging {

  /**
    * Collect JavaFX transforms from a node upwards to scene root.
    */
  def getTransformsToRoot(start: Node, root: Node): IndexedSeq[Transform] = {
    val seqBuilder = IndexedSeq.newBuilder[Transform]

    var parent: Node = start
    do {
      if (parent == null) {
        // TODO: This should be fixed now. Remove this fragile check?
        error(s"getTransformsToRoot: Null parent. start:$start, root:$root")
      } else {
        seqBuilder += parent.getLocalToParentTransform
        parent = parent.getParent
      }
    } while (parent != root && parent != null)

    seqBuilder.result()
  }

  def boundsInRoot(child: Node, root: Node): Bounds = {
    val transforms = getTransformsToRoot(child, root)
    val boundsInRoot = xform(transforms, child.getBoundsInLocal)
    boundsInRoot
  }

  def combine(a: Bounds, b: Bounds): BoundingBox = {

    val newMinX = Math.min(a.getMinX, b.getMinX)
    val newMinY = Math.min(a.getMinY, b.getMinY)
    val newMinZ = Math.min(a.getMinZ, b.getMinZ)

    val newMaxX = Math.max(a.getMaxX, b.getMaxX)
    val newMaxY = Math.max(a.getMaxY, b.getMaxY)
    val newMaxZ = Math.max(a.getMaxZ, b.getMaxZ)

    new BoundingBox(newMinX, newMinY, newMinZ,
      newMaxX - newMinX, newMaxY - newMinY, newMaxZ - newMinZ)
  }

  /**
    * Transform a point by a stack of JavaFX transforms.
    */
  def xform(transforms: Iterable[Transform], x: Double, y: Double): Point2D = {
    transforms.foldLeft(new javafx.geometry.Point2D(x, y))((a, b) => b.transform(a.getX, a.getY))
  }

  /**
    * Transform a JavaFX bounding box by a stack of JavaFX transforms.
    */
  def xform(transforms: Iterable[Transform], bounds: Bounds): Bounds = {
    transforms.foldLeft(bounds)((a, b) => b.transform(a))
  }

  def interpolate(x0: Double, x1: Double, f: Double) = x0 + (x1 - x0) * f

  def interpolateX(bounding: Bounds, f: Double) = interpolate(bounding.getMinX, bounding.getMaxX, f)

  def interpolateY(bounding: Bounds, f: Double) = interpolate(bounding.getMinY, bounding.getMaxY, f)


}
