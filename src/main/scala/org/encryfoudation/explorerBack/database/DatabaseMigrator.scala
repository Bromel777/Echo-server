package org.encryfoudation.explorerBack.database

trait DatabaseMigrator[F[_]] {
  def migrate(databaseUrl: String,
              databaseLogin: String,
              databasePassword: String): F[Int]
}
