package fi.pelam.javafxactor

import java.util.concurrent._
import javafx.application.Platform

import grizzled.slf4j.Logging

import scala.concurrent.ExecutionContextExecutor

/**
  * Akka service to run certain actors in same thread with JavaFx UI.
  *
  * This allows the actors to create JavaFx stages etc.
  *
  * This is singleton for simplicity.
  */
object JavaFxDispatcher
  extends AbstractExecutorService with ExecutionContextExecutor
    with Logging {

  @volatile
  private[this] var shutdownRequested = false

  info(s"JavaFX Dispatcher starting. ${Thread.currentThread()}")

  JavaFxApplication.launch()

  info(s"JavaFX Dispatcher ready. ${Thread.currentThread()}")

  override def shutdown(): Unit = {
    info(s"JavaFX Dispatcher shutdown requested. ${Thread.currentThread()}")
    if (!shutdownRequested) {
      info(s"JavaFX Dispatcher shutdown requested for the first time. Requesting JavaFX shutdown. ${Thread.currentThread()}")

      shutdownRequested = true

      Platform.exit()
    }
    info(s"JavaFX Dispatcher shutdown complete. ${Thread.currentThread()}")
  }

  override def isTerminated: Boolean = !JavaFxApplication.isRunning

  override def awaitTermination(timeout: Long, unit: TimeUnit): Boolean = {
    info(s"JavaFX Dispatcher shutdownLatch wait starting. ${Thread.currentThread()}")
    val value = JavaFxApplication.shutdownLatch.await(timeout, unit)
    info(s"JavaFX Dispatcher shutdownLatch released. ${Thread.currentThread()}")
    value
  }

  override def shutdownNow(): java.util.List[Runnable] = this.synchronized {
    shutdown()

    // No idea how far JavaFX got
    java.util.Collections.emptyList[Runnable]
  }

  override def isShutdown: Boolean = shutdownRequested || !JavaFxApplication.isRunning

  override def execute(command: Runnable): Unit = {

    if (!JavaFxApplication.isRunning) {
      throw new RejectedExecutionException("JavaFX app is not running. rejecting " + command)
    }

    Platform.runLater(command)
  }

  override def reportFailure(cause: Throwable): Unit = throw cause
}


