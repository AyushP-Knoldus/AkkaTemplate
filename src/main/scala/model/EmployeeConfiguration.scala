package com.knoldus
package model

case class EmployeeConfiguration(app: ApplicationConf,
                                 akka: AkkaConfig,
                                 dbConfig: DBConfig)

case class DBConfig(profile: String,
                    driver: String,
                    url: String,
                    user: String,
                    password: String,
                    schema: String,
                    threadsPoolCount: Int,
                    queueSize: Int)

case class ApplicationConf(host: String, port: Int)

case class AkkaConfig(futureAwaitDurationMins: Int, akkaWorkersCount: Int)
