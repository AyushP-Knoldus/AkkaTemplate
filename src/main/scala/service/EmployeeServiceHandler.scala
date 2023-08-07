package com.knoldus
package service

import actors.EmployeeActor._
import model.EmployeeRecords
import persistence.util.JsonHelper

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

trait EmployeeServiceHandler extends JsonHelper {
  implicit val system: ActorSystem

  implicit val materializer: ActorMaterializer
  implicit val timeOut: Timeout = Timeout(40.seconds)

  import akka.pattern.ask
  import system.dispatcher

  def getEmployeeList(command: ActorRef): Future[HttpResponse] = {
    ask(command, GetListOfEmployees()).map {
      case result: Seq[EmployeeList] =>
        HttpResponse(
          StatusCodes.OK,
          entity = HttpEntity(
            ContentTypes.`application/json`,
            write(result)
          ))
    }
  }
  def addNewEmployee(command: ActorRef,request: EmployeeRecords): Future[HttpResponse] = {
    ask(command,InputEmployeeData(request)).flatMap {
      case Updated(true) =>
        Future.successful(HttpResponse(status = StatusCodes.OK,
          entity = write("New_Employee_Added")))
      case Updated(false) =>
        Future.successful(HttpResponse(status = StatusCodes.Conflict,
          entity = write("Employee_Not_Added")))
    }
  }

  def updateEmployeeDetails(command: ActorRef,request: EmployeeRecords): Future[HttpResponse] = {
    ask(command,UpdateEmployeeDetails(request)).flatMap {
      case Updated(true) =>
        Future.successful(HttpResponse(status = StatusCodes.OK,
          entity = write("Employee_Details_Updated")))
      case Updated(false) =>
        Future.successful(HttpResponse(status = StatusCodes.Conflict,
          entity = write("Employee_Does_Not_Exist")))
    }
    }

  def deleteEmployee(command: ActorRef,request: String): Future[HttpResponse] = {
    ask(command,DeleteEmployeeRecord(request)).flatMap {
      case Updated(true) =>
        Future.successful(HttpResponse(status = StatusCodes.OK,
          entity = write("Employee_Records_Deleted")))
      case Updated(false) =>
        Future.successful(HttpResponse(status = StatusCodes.Conflict,
          entity = write("Unable_To_Delete_Employee_Records")))
    }
  }

}