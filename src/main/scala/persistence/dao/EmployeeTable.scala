package com.knoldus
package persistence.dao

import model.EmployeeRecords
import persistence.db.DatabaseApi.api._

import slick.lifted.ProvenShape

import java.time.OffsetDateTime

final class EmployeeTable(tag: Tag)
  extends Table[EmployeeRecords](tag,Some("akka_template"),"employeerecords") {

  def employeeId: Rep[String] = column[String]("employee_id")

  def employeeName: Rep[String] = column[String]("name")

  def dateOfJoining: Rep[OffsetDateTime] = column[OffsetDateTime]("doj")

  def employeeSalary: Rep[Double] = column[Double]("salary")

  def employeeDesignation: Rep[String] = column[String]("designation")

  def * : ProvenShape[EmployeeRecords] =
    (
      employeeId,
      employeeName,
      dateOfJoining,
      employeeSalary,
      employeeDesignation
    ).shaped <> (EmployeeRecords.tupled, EmployeeRecords.unapply)
}
