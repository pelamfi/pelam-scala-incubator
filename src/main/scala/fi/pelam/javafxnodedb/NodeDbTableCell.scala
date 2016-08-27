package fi.pelam.javafxnodedb

import javafx.scene.control.TableCell

/**
  * Catch the the logical position `MarkerPositionKey` from `MarkableCellValue`
  * and associate it with this cell. Report to `CellMarkerDb` when this cell
  * is layouted.
  *
  * This complex arrangement makes it possible for `CellMarkerDb` to draw
  * markers on top of correct cells in correct time in correct coordinates.
  *
  * Idea is that a `javafx.util.Callback` producing cells derived from
  * this class is set to `setCellFactory` of a `javafx.scene.control.TableColumn`
  * used in the table where markers drawn on top are needed.
  */
abstract class NodeDbTableCell[K <: AnyRef, R, V <: NodeKeyProvider[K]]
  extends TableCell[R, V] with NodeDbJavaFxNode[K] {

  var key: Option[K] = None

  /**
    * manipulate cell color here: setStyle("-fx-background-color: chocolate")
    * and do setText("text")
    */
  def updateCellValue(cellValue: V)

  /**
    * This method should clean up all state in this cell and
    * reset all property changes back to defaults.
    */
  def clearCellValue(): Unit

  final override def updateItem(cellValue: V, empty: Boolean) = {
    super.updateItem(cellValue, empty)

    if (empty) {
      // Cells are recycled.
      // This cleanup is very important:
      // https://community.oracle.com/thread/3569769
      // http://stackoverflow.com/questions/23180428/tableview-items-are-not-removed-but-do-not-respond/23183658#23183658
      removeHook()
      key = None
      clearCellValue()
    } else {
      key = Some(cellValue.nodeKey)
      updateCellValue(cellValue)

      // TODO: Why does calling the usedHook in _both_ layoutChildren and here seem
      // to help with the marker placement bug.
      // TODO: Is this still needed?
      usedHook()
    }

  }

  def usedHook()

  def removeHook()

  override def layoutChildren() = {
    super.layoutChildren()
    usedHook()
  }

  override def toString = s"cell $key"
}

