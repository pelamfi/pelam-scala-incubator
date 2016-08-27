package fi.pelam.actorutil

import akka.actor.Scheduler

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.Deadline.now
import scala.concurrent.duration._

final class Throttle private(func: => Unit, delay: FiniteDuration)(implicit scheduler: Scheduler,
  executor: ExecutionContextExecutor) extends ((Unit) => Unit) {

  private var deadline: Option[Deadline] = None

  private def check(): Unit = {
    for (deadline <- deadline) {

      val call = this.synchronized {
        if (deadline.isOverdue()) {
          this.deadline = None
          true
        } else {

          try {
            scheduler.scheduleOnce(deadline.timeLeft)(check())(executor)
            false
          } catch {
            case e: IllegalStateException if e.getMessage.endsWith("shutdown") => true
          }

        }
      }

      if (call) {
        func
      }
    }
  }

  override def apply(v1: Unit): Unit = {
    this.synchronized {
      deadline = Some(now + delay)
      scheduler.scheduleOnce(delay)(check())(executor)
    }
  }
}

object Throttle {
  def apply(func: => Unit)(
    implicit scheduler: Scheduler, executor: ExecutionContextExecutor): Throttle =
    apply(300.milliseconds)(func)(scheduler, executor)

  def apply(delay: FiniteDuration = 300.milliseconds)(func: => Unit)(
    implicit scheduler: Scheduler, executor: ExecutionContextExecutor): Throttle =
    new Throttle(func, delay)(scheduler, executor)

}
