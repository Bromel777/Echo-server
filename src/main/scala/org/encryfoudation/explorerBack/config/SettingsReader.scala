package org.encryfoudation.explorerBack.config

import cats.effect.IO
import com.typesafe.config.{Config, ConfigFactory}
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._

case class Settings(postgres: PostgresSettings)

object SettingsReader {

  def read: IO[Settings] = IO {
    ConfigFactory
      .load( "local.conf" )
      .withFallback( ConfigFactory.load() )
      .as[Settings]("explorer")
  }

}
