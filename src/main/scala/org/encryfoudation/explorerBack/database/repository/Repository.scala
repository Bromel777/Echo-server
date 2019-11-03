package org.encryfoudation.explorerBack.database.repository

import fs2.Stream
import org.encryfoudation.explorerBack.database.data.Node

trait Repository[F[_]] {

  def getNodes: Stream[F, Node]
}
