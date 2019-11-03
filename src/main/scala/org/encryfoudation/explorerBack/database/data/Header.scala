package org.encryfoudation.explorerBack.database.data

case class Header(id: String,
                  version: Byte,
                  parentId: String,
                  transactionsRoot: String,
                  timestamp: Long,
                  height: Int,
                  nonce: Long,
                  difficulty: Long,
                  stateRoot: String,
                  equihashSolution: List[Int])
