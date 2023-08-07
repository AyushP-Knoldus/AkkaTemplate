package com.knoldus
package service

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import actors.EmployeeActor.{DeleteEmployeeRecord, InputEmployeeData, UpdateEmployeeDetails}

import com.knoldus.model.EmployeeRecords
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

trait EmployeeService extends EmployeeServiceHandler {


  private def employeeGetRoutes(command: ActorRef): Route =
    pathPrefix("employee") {
      get {
        path("get-employee-list") {
          val result = getEmployeeList(command)
          complete(result)
        }
      }
      }
  private def employeePostRoutes(command: ActorRef): Route =
    pathPrefix("employee") {
        path("add-new-employee") {
          pathEnd {
            (post & entity(as[EmployeeRecords])) { request =>
              val response = addNewEmployee(command,request)
              complete(response)
          }
        }
      }~
      path("update-employee-details") {
        pathEnd {
          (post & entity(as[EmployeeRecords])) { request =>
            val response = updateEmployeeDetails(command,request)
            complete(response)
          }
        }
      }~
      path("delete-employee-record") {
        pathEnd {
          (post & entity(as[DeleteEmployeeRecord])) { request =>
            val response = deleteEmployee(command, request.id)
            complete(response)
          }
        }
      }
    }
  def getUserRoutes(command: ActorRef): Route = employeeGetRoutes(command) ~ employeePostRoutes(command)
}
