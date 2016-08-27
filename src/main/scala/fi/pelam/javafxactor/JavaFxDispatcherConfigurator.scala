package fi.pelam.javafxactor

import java.util.concurrent.{ExecutorService, ThreadFactory}

import akka.dispatch.{DispatcherPrerequisites, ExecutorServiceConfigurator, ExecutorServiceFactory}
import com.typesafe.config.Config

class JavaFxDispatcherConfigurator(config: Config, prerequisites: DispatcherPrerequisites)
  extends ExecutorServiceConfigurator(config, prerequisites) {

  def createExecutorServiceFactory(id: String, threadFactory: ThreadFactory) = new ExecutorServiceFactory() {

    override def createExecutorService: ExecutorService = JavaFxDispatcher
  }
}
