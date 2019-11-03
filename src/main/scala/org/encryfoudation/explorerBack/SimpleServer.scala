package org.encryfoudation.explorerBack

import cats.effect._
import doobie.util.transactor.Transactor
import org.encryfoudation.explorerBack.database.FlywayDatabaseMigrator
import doobie.util.ExecutionContexts
import org.encryfoudation.explorerBack.config.SettingsReader
import org.encryfoudation.explorerBack.database.repository.DoobieRepository
import org.encryfoudation.explorerBack.routes.NodeRoutes
import org.http4s.dsl.impl.Root
import org.http4s.server.Router
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.blaze._
import cats.implicits._

object SimpleServer extends IOApp {

  val migrator = new FlywayDatabaseMigrator

  implicit val cs = IO.contextShift(ExecutionContexts.synchronous)

  override def run(args: List[String]): IO[ExitCode] = for {
    config <- SettingsReader.read
    _ <- migrator.migrate(
          config.postgres.host,
          config.postgres.login,
          config.postgres.pass
        )
    tx = Transactor.fromDriverManager[IO](
      "org.postgresql.Driver",
          config.postgres.host,
          config.postgres.login,
          config.postgres.pass,
        )
    doobieRepo = DoobieRepository(tx)
    routes = NodeRoutes(doobieRepo).routes
    httpApi = Router("/" -> routes).orNotFound
    server <- BlazeServerBuilder[IO]
      .bindHttp(1000, "0.0.0.0")
      .withHttpApp(httpApi)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
  } yield server
}