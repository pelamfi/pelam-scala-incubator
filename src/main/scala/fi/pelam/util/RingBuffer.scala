package fi.pelam.util

/**
  * A simple Scala friendly RingBuffer with cargo culted
  * ideas from LMAX Disruptor.
  *
  * This is simpler than the LMAX buffer, because both read and write
  * operations only support polling.
  * The Disruptor RingBuffer support for polling readers is underway
  * https://github.com/LMAX-Exchange/disruptor/issues/84
  *
  * Disclaimer: Actual performance comparison and testing to be done.
  *
  */
final class RingBuffer[T <: AnyRef](eventMaker: () => T, val capacity: Int = 1024)(implicit tag: scala.reflect.ClassTag[T]) {

  // Padding to 64 bytes to make sure object header, and reader and
  // writer positions and the buffer pointer are on separate cache lines.
  var pad1a: Long = capacity
  var pad2a: Long = capacity
  var pad3a: Long = capacity
  var pad4a: Long = capacity
  var pad5a: Long = capacity
  var pad6a: Long = capacity
  var pad7a: Long = capacity

  @volatile
  private[this] var writeNumber: Long = 0

  var pad1b: Long = capacity
  var pad2b: Long = capacity
  var pad3b: Long = capacity
  var pad4b: Long = capacity
  var pad5b: Long = capacity
  var pad6b: Long = capacity
  var pad7b: Long = capacity

  @volatile
  private[this] var readNumber: Long = 0

  var pad1c: Long = capacity
  var pad2c: Long = capacity
  var pad3c: Long = capacity
  var pad4c: Long = capacity
  var pad5c: Long = capacity
  var pad6c: Long = capacity
  var pad7c: Long = capacity

  private[this] val buffer: Array[T] = tag.newArray(capacity)

  private[this] def readSlot: Int = (readNumber % capacity).toInt

  private[this] def writeSlot: Int = (writeNumber % capacity).toInt

  def used = (writeNumber - readNumber).toInt

  def write(valueUpdater: T => Unit): Boolean = {
    if (used < capacity) {
      if (buffer(writeSlot) == null) {
        buffer(writeSlot) = eventMaker()
      }
      valueUpdater(buffer(writeSlot))
      writeNumber += 1
      true
    } else {
      false
    }
  }

  def read(valueReader: T => Unit): Boolean = {
    if (used > 0) {
      valueReader(buffer(readSlot))
      readNumber += 1
      true
    } else {
      false
    }
  }

  def readAll(valueReader: T => Unit) = {
    while (used > 0) {
      valueReader(buffer(readSlot))
      readNumber += 1
    }
  }

  override def toString: String = {
    s"[RingBuffer capacity:$capacity, used: $used, readNumber: $readNumber, writeNumber: $writeNumber]"
  }
}
