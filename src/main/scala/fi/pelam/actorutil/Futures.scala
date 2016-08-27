package fi.pelam.actorutil

import akka.actor.Actor

trait Futures {
  this: Actor =>

  // To make akka ask pattern work
  implicit val executionContext = context.system.dispatcher
}
