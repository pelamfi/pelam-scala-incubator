package fi.pelam.util

import java.time.{Instant, LocalDateTime, ZoneId}

object TimeUtil {
  val utc = ZoneId.of("UTC")

  //noinspection LanguageFeature
  implicit def toFiniteDuration(d: java.time.Duration): scala.concurrent.duration.FiniteDuration =
  scala.concurrent.duration.Duration.fromNanos(d.toNanos)

  def toLocalString(timestamp: Instant): String = {
    LocalDateTime.ofInstant(timestamp, ZoneId.systemDefault()).toString
  }

  def toUtcString(timestamp: Instant): String = {
    LocalDateTime.ofInstant(timestamp, utc).toString
  }

}
