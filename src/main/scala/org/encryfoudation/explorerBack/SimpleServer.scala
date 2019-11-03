package org.encryfoudation.explorerBack

import cats.effect._
import cats.implicits._
import org.encryfoudation.explorerBack.database.FlywayDatabaseMigrator

object SimpleServer extends IOApp {

  val migrator = new FlywayDatabaseMigrator

  override def run(args: List[String]): IO[ExitCode] =
    migrator.migrate(

    ) *> IO(ExitCode.Success)

}