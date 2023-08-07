CREATE SCHEMA IF NOT EXISTS akka_template;

CREATE TABLE IF NOT EXISTS akka_template.employeerecords (
employee_id VARCHAR PRIMARY KEY,
name VARCHAR not null,
doj TIMESTAMP not null,
salary numeric(7,2) not null,
designation varchar not null);
