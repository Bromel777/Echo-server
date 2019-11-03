package org.encryfoudation.explorerBack.database.repository

import cats.effect.Sync
import doobie.implicits._
import doobie.util.transactor.Transactor
import org.encryfoudation.explorerBack.database.data.Node

case class DoobieRepository[F[_]: Sync](tx: Transactor[F]) extends Repository[F] {
  override def getNodes: fs2.Stream[F, Node] =
    sql"select * from Nodes"
    .query[Node]
    .stream
    .transact(tx)
}
