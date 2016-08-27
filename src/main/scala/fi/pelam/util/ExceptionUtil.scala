package fi.pelam.util

import java.io.{PrintWriter, StringWriter}

object ExceptionUtil {

  // http://stackoverflow.com/a/1149712/1148030
  def stacktraceToString(t: Throwable): String = {
    val sw = new StringWriter()
    val pw = new PrintWriter(sw)
    t.printStackTrace(pw)
    sw.toString
  }

}
