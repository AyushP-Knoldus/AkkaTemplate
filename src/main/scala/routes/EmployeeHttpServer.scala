package com.knoldus
package routes
import akka.actor.{ActorRef, ActorSystem}
import akka.stream.ActorMaterializer
import model.EmployeeConfiguration
import persistence.db.DatabaseApi.api._
import actors.EmployeeActor

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import service.EmployeeService
import com.knoldus.persistence.dao.Dao

import pureconfig.ConfigSource
import pureconfig.error.ConfigReaderFailures
import pureconfig.generic.auto._
import slick.util.AsyncExecutor

import scala.util.{Failure, Success}

object EmployeeHttpServer extends App with EmployeeService {

  implicit val system: ActorSystem = ActorSystem("employee")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  import system.dispatcher

  val config: EmployeeConfiguration = ConfigSource
    .resources("application.conf")
    .withFallback(ConfigSource.systemProperties)
    .load[EmployeeConfiguration] match {
    case Left(e: ConfigReaderFailures) =>
      throw new RuntimeException(
        s"Unable to load config, original error: ${e.prettyPrint()}")
    case Right(x) => x
  }

  //val schema: String = config.dbConfig.schema

  implicit val db: Database = Database.forURL(
    config.dbConfig.url,
    user = config.dbConfig.user,
    password = config.dbConfig.password,
    driver = config.dbConfig.driver,
    executor = AsyncExecutor("postgres",
      numThreads = config.dbConfig.threadsPoolCount,
      queueSize = config.dbConfig.queueSize)
  )

  val dao = new Dao()
  val employeeActor: ActorRef =
    system.actorOf(EmployeeActor.props(dao), "employee")

  lazy val routes: Route = getUserRoutes(employeeActor)

  val binding = Http().bindAndHandle(routes, config.app.host, config.app.port)
  binding.onComplete {
    case Success(binding) ⇒
      val localAddress = binding.localAddress
      println(localAddress + " Sucesssful Binding")

    case Failure(exception) => println(exception.getMessage)
  }
}
