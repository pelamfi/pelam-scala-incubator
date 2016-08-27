package fi.pelam.nodedb

import org.junit.Assert._
import org.junit.Test

import scala.collection.mutable

abstract class DbTypes {

  abstract class BaseKey {
  }

  type Key <: BaseKey
  type Value <: BaseValue

  abstract class BaseValue {
    val key: Key
  }

  abstract class Db {
    val map: mutable.Map[Key, Value] = mutable.Map()

    def get(k: Key): Value = map(k)

    def put(v: Value) = {
      map.put(v.key, v)
    }
  }

}

object ConcreteTypes extends DbTypes {
  override type Key = ConcreteKey
  override type Value = ConcreteValue

  final class ConcreteDb extends Db {
  }

  case class ConcreteKey(i: Int) extends BaseKey

  case class ConcreteValue(s: String)(override val key: ConcreteKey) extends BaseValue {
  }

}

class CellMarkerDbTypesTest {

  import ConcreteTypes._

  @Test
  def test() = {
    val db = new ConcreteDb()
    db.put(ConcreteValue("foo")(ConcreteKey(0)))
    assertEquals(ConcreteValue("foo")(ConcreteKey(2)), db.get(ConcreteKey(0)))
  }
}