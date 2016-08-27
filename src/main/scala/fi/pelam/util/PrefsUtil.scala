package fi.pelam.util

import java.util.prefs.Preferences

object PrefsUtil {

  def ifDefinedDouble(preferences: Preferences, key: String)(function: (Double) => Unit): Unit = {
    val stringVal = preferences.get(key, null)
    if (stringVal != null) {
      // Will be zero if value can't be parsed as double
      function(preferences.getDouble(key, 0))
    }
  }

  def ifDefined(preferences: Preferences, key: String)(function: (String) => Unit): Unit = {
    Option(preferences.get(key, null.asInstanceOf[String])).foreach(function(_))
  }
}
