package com.knoldus
package persistence.dao

import model.EmployeeRecords
import persistence.db.DatabaseApi.api._

import slick.lifted.TableQuery

import scala.concurrent.Future

class Dao(implicit val db: Database) {
  val employeeTableQuery = TableQuery[EmployeeTable]

  def addNewEmployee(employeeDetails: EmployeeRecords): Future[Int] = {
    db.run(employeeTableQuery += employeeDetails)
  }

  def listAllEmployee: Future[Seq[EmployeeRecords]] = {
    db.run(employeeTableQuery.result)
  }

  def updateEmployeeEntry(newEmployeeRecords: EmployeeRecords): Future[Int] = {
    val query = employeeTableQuery
      .filter(_.employeeId === newEmployeeRecords.employeeID)
      .update(newEmployeeRecords)
    db.run(query)
  }

  def deleteEmployeeRecord(id: String): Future[Int] = {
    val query = employeeTableQuery
      .filter(_.employeeId === id)
      .delete
    db.run(query)
  }
  }

