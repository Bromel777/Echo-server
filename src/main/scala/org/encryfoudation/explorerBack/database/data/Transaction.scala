package org.encryfoudation.explorerBack.database.data

case class Transaction(id: String,
                       fee: Long,
                       timestamp: Long,
                       defaultProofOpt: Option[String],
                       isCoinbase: Boolean,
                       blockId: String)
