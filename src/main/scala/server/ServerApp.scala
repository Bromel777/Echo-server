package server

import java.io.{BufferedReader, BufferedWriter, InputStreamReader, OutputStreamWriter}
import java.net.{ServerSocket, Socket}
import cats.effect._
import cats.effect.syntax.all._
import cats.effect.ExitCase._
import cats.implicits._
import cats.effect.{ExitCode, IO, IOApp, Sync}

object ServerApp extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    def close[F[_]: Sync](socket: ServerSocket): F[Unit] =
      Sync[F].delay(socket.close()).handleErrorWith(_ => Sync[F].unit)

    IO(new ServerSocket(args.headOption.map(_.toInt).getOrElse(5505)))
      .bracket(
        serverSocket => Utils.server[IO](serverSocket) >> IO.pure(ExitCode.Success)
      )(
        serverSocket => close[IO](serverSocket) >> IO(println("Server finished"))
      )
  }
}
