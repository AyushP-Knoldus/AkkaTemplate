package com.knoldus
package actors

import akka.actor.{Actor, Props}
import model.EmployeeRecords

import akka.pattern.pipe
import com.knoldus.persistence.dao.Dao

import java.time.OffsetDateTime
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class EmployeeActor(dao: Dao) extends Actor {

  import EmployeeActor._

  override def receive: Receive = {
    case InputEmployeeData(employee) =>
      val result: Future[Updated] = addNewEmployee(employee: EmployeeRecords)
      result.pipeTo(sender())

    case GetListOfEmployees() =>
      val result: Future[Seq[EmployeeList]] = getEmployeeList
      result.pipeTo(sender())

    case UpdateEmployeeDetails(newEmployeeRecords) =>
      val result: Future[Updated] = updateEmployeeDetails(newEmployeeRecords)
      result.pipeTo(sender())

    case DeleteEmployeeRecord(id) =>
      val result: Future[Updated] = deleteEmployeeRecord(id)
      result.pipeTo(sender())
  }

  def addNewEmployee(records: EmployeeRecords): Future[Updated] = {
    updateDatabase(dao.addNewEmployee(records))
  }

  def getEmployeeList: Future[Seq[EmployeeList]] = {
    dao.listAllEmployee.map {
      records =>
        records.map { entry =>
          EmployeeList(
            entry.employeeID,
            entry.name,
            entry.doj,
            entry.salary,
            entry.designation
          )
        }
    }
  }

  def updateEmployeeDetails(newEmployeeRecords: EmployeeRecords): Future[Updated] = {
    updateDatabase(dao.updateEmployeeEntry(newEmployeeRecords))
  }

  def deleteEmployeeRecord(id: String): Future[Updated] = {
    updateDatabase(dao.deleteEmployeeRecord(id))
  }

  private def updateDatabase(daoResponse: Future[Int]): Future[Updated] = {
    daoResponse.map {
      case 0 => Updated(false)
      case 1 => Updated(true)
    }
  }
}
object EmployeeActor {

  final case class InputEmployeeData(employeeDetails: EmployeeRecords)

  final case class Updated(boolean: Boolean)

  final case class GetListOfEmployees()

  final case class EmployeeList(employeeID: String,
                                name: String,
                                doj: OffsetDateTime,
                                salary: Double,
                                designation: String)

  final case class UpdateEmployeeDetails(newEmployeeRecords: EmployeeRecords)

  final case class DeleteEmployeeRecord(id: String)

  def props(dao: Dao): Props =
    Props(new EmployeeActor(dao))

}
