package server

import java.io.{BufferedReader, BufferedWriter, InputStreamReader, OutputStreamWriter}
import java.net.{ServerSocket, Socket}
import cats.effect._
import cats.effect.syntax.all._
import cats.effect.ExitCase._
import cats.implicits._
import java.net.{ServerSocket, Socket}

object Utils {

  def server[F[_]: Concurrent](serverSocket: ServerSocket): F[Unit] = {
    def close(socket: Socket): F[Unit] =
      Sync[F].delay(socket.close()).handleErrorWith(_ => Sync[F].unit)

    for {
      _ <- Sync[F]
          .delay(serverSocket.accept())
          .bracketCase{
            socket => echoProtocol(socket).guarantee(close(socket)).start
          }{ case (socket, exit) => exit match {
            case Completed => Sync[F].unit
            case Error(_) | Canceled => close(socket)
          }}
      _ <- server(serverSocket)
    } yield ()
  }

  def echoProtocol[F[_]: Sync](clientSocket: Socket): F[Unit] = {

    def reader(clientSocket: Socket): Resource[F, BufferedReader] = Resource.make(
      Sync[F].delay(new BufferedReader(new InputStreamReader(clientSocket.getInputStream)))
    )(bfReader => Sync[F].delay(bfReader.close()).handleErrorWith(_ => Sync[F].unit))

    def writer(clientSocket: Socket): Resource[F, BufferedWriter] = Resource.make(
      Sync[F].delay(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream)))
    )(bfWriter => Sync[F].delay(bfWriter.close()).handleErrorWith(_ => Sync[F].unit))

    def readerAndWriter(clientSocket: Socket): Resource[F, (BufferedReader, BufferedWriter)] =
      for {
        bfReader <- reader(clientSocket)
        bfWriter <- writer(clientSocket)
      } yield (bfReader, bfWriter)

    def loop(reader: BufferedReader, writer: BufferedWriter): F[Unit] = for {
      line <- Sync[F].delay(reader.readLine())
      _ <- line match {
        case "" => Sync[F].unit
        case _ =>  Sync[F].delay{
          writer.write(line)
          writer.newLine()
          writer.flush()
        } >> loop(reader, writer)
      }
    } yield ()

    readerAndWriter(clientSocket).use{
      case (reader, writer) => loop(reader, writer)
    }
  }
}
