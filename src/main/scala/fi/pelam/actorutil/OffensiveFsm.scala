package fi.pelam.actorutil

import akka.actor.{FSM, OneForOneStrategy, SupervisorStrategy}
import grizzled.slf4j.Logging

/**
  * Derived version of FSM belonging to Akka. This version behaves "offensively"
  * instead of defensively.
  *
  * It crashes hard instead of just maybe logging a warning.
  *
  * This offensiveness is especially desirable during development,
  * when fixing and detecting problems early is usually much cheaper
  * than going to production with them.
  *
  * This actor does also basic logging through SLF4J.
  */
// TODO: http://doc.akka.io/docs/akka/snapshot/java/logging.html
trait OffensiveFsm[S, D] extends FSM[S, D] with Logging {
  override val supervisorStrategy =
    OneForOneStrategy(loggingEnabled = false)({
      case t: Throwable =>
        error(s"OffensiveFsm supervisorStrategy escalating an exception ${t.getClass.getSimpleName} " +
          s"in actor ${sender.path}.")

        SupervisorStrategy.Escalate
    })

  def handleErrorEvent(event: Any) = {
    error(s"Unexpected FSM event $event from ${sender.path}.\n" +
      s"FSM ${self.path.name} is in $stateName. ")

    if (OffensiveFsm.offensiveMode) {
      // Shutdown the whole system on first error
      context.system.terminate()
    }

    stay
  }

  // Raise error on unhandled messages (called events in FSM base class)
  whenUnhandled {
    case event: Any =>
      handleErrorEvent(event)
  }

  // Raise error when stop event comes in, except for 2
  // first cases which are ok and normal.
  onTermination {
    case StopEvent(FSM.Shutdown, _, _) => Unit
    case StopEvent(FSM.Normal, _, _) => Unit
    case s: StopEvent =>
      error(s"Unexpected FSM stop event $s.")

      handleError()

      stay
  }

  def handleError(): Unit = {
    if (OffensiveFsm.offensiveMode) {
      sys.error("Error in actor. See log above.")
    } else {
      error("Error in actor. See log above.")
    }
  }

  onTransition {
    case (from: Any, to: Any) => info(s"${self.path.name} state: $from -> $to")
  }

}

object OffensiveFsm {

  /**
    * Set this flag to false to globally change OffensiveFsm
    * into a "production mode", which is basically same as the
    * usual Akka FSM, except with logging about unhanled events.
    */
  var offensiveMode: Boolean = true

}
