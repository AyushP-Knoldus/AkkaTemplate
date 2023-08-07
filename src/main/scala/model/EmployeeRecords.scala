package com.knoldus
package model

import java.time.OffsetDateTime


final case class EmployeeRecords(
                                  employeeID: String,
                                  name: String,
                                  doj: OffsetDateTime,
                                  salary: Double,
                                  designation: String
               )
