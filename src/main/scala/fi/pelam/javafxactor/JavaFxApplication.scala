package fi.pelam.javafxactor

import java.util.concurrent.{CountDownLatch, TimeUnit}
import javafx.application.Application
import javafx.stage.Stage
import javax.annotation.concurrent.GuardedBy

import grizzled.slf4j.Logging

/**
  * Basically a dummy application with static
  * initialization to allow JavaFxDispatcher
  * guarantee that JavaFx Platform.runLater will work.
  */
object JavaFxApplication extends Logging {

  @GuardedBy("this")
  private var javaFxStartedLatch = new CountDownLatch(0)

  @GuardedBy("this")
  private[this] var javaFxShutdownLatch = new CountDownLatch(0)

  object LauncherRunnable extends Runnable with Logging {

    override def run(): Unit = {
      info("JavaFX thread starting")

      try {
        Application.launch(classOf[JavaFxApplication])
      } catch {
        case t: Throwable => error("JavaFx launch threw exception", t)
      }

      info("JavaFX thread ending")
      javaFxShutdownLatch.countDown()
    }

  }

  def resetLatches() = this.synchronized {
    javaFxStartedLatch = new CountDownLatch(1)
    javaFxShutdownLatch = new CountDownLatch(1)
  }

  def shutdownLatch = this.synchronized {
    javaFxShutdownLatch
  }

  def startedLatch = this.synchronized {
    javaFxStartedLatch
  }

  def isRunning = this.synchronized {
    // started
    javaFxStartedLatch.getCount == 0 &&
      // and not shutdown yet
      javaFxShutdownLatch.getCount == 1
  }

  /**
    * Blocks until JavaFx is up and running and Platform.runLater is ready
    * take orders.
    */
  def launch(): Unit = {

    if (!isRunning) {
      info("Starting JavaFX thread.")
      resetLatches()

      // This initializes JavaFX so Platform.runLater does not throw.
      val launcherThread = new Thread(LauncherRunnable, "javafx-launcher")

      launcherThread.setDaemon(true)

      launcherThread.start()

      if (!javaFxStartedLatch.await(10, TimeUnit.SECONDS)) {
        sys.error("JavaFX app did not become initialized.")
      }

      info("JavaFX started signal received.")
    }

  }

}

class JavaFxApplication extends Application with Logging {

  override def start(primaryStage: Stage): Unit = {
    info("JavaFX running")
    JavaFxApplication.javaFxStartedLatch.countDown()
  }

  override def stop(): Unit = {
    info("JavaFX stop received")
    super.stop()
  }
}
