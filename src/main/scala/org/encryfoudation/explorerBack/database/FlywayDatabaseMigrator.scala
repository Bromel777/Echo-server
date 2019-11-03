package org.encryfoudation.explorerBack.database

import cats.effect.IO
import org.flywaydb.core.Flyway

class FlywayDatabaseMigrator[F[_]] extends DatabaseMigrator[IO] {
  override def migrate(databaseUrl: String,
                       databaseLogin: String,
                       databasePassword: String): IO[Int] = IO {
    val flyway = new Flyway()
    flyway.setDataSource(databaseUrl, databaseLogin, databasePassword)
    flyway.migrate()
  }
}
