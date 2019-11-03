package org.encryfoudation.explorerBack.routes

import cats.effect.Sync
import org.encryfoudation.explorerBack.database.repository.Repository
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

final case class NodeRoutes[F[_]: Sync](repository: Repository[F]) extends Http4sDsl[F] {

  val routes = HttpRoutes.of[F]{
    case GET -> Root / "nodes" =>
      Ok(repository.getNodes.map(_.toString))
  }

}
