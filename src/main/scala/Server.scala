import cats.effect.IO
import org.http4s.dsl.io.{->, /, GET, InternalServerError, Ok, Root, _}
import org.http4s.{HttpRoutes, Request, Response}
import org.typelevel.log4cats.Logger
import cats.data.Kleisli

import org.http4s.client.Client
import org.http4s.dsl.io._
import org.http4s.implicits.http4sLiteralsSyntax
import io.circe.syntax.EncoderOps

object Server extends Serde {

  def helloWorldService(client: Client[IO])(implicit databaseContract: DatabaseContract, logger: Logger[IO]): Kleisli[IO, Request[IO], Response[IO]] = HttpRoutes.of[IO] {
    case GET -> Root =>
      val target = uri"http://localhost:8080/hello/" / "Bob"
      val result = client.expect[String](target)
      Ok(result)
    case GET -> Root / "getFirst" =>
      databaseContract.getCountry
        .map(country => Ok(country.asJson.noSpaces))
        .leftMap {
          case DatabaseError.ObjectNotFoundError(msg) =>
            Ok(s"Object not found $msg")
          case DatabaseError.UnknownDatabaseError(msg) =>
            InternalServerError(msg)

        }.value.flatMap(_.merge)
    case GET -> Root / "hello" / name =>
      Ok(s"Hello, $name.")
  }.orNotFound
}
