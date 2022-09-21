import DataModel.ContentResponse
import cats.effect.IO
import org.http4s.dsl.io.{->, /, GET, InternalServerError, Ok, Root, _}
import org.http4s.{AuthScheme, Credentials, Header, HttpRoutes, MediaType, Request, Response}
import org.typelevel.log4cats.Logger
import cats.data.Kleisli
import io.circe.{Json, parser}
import org.http4s.client.Client
import org.http4s.dsl.io._
import org.http4s.implicits.http4sLiteralsSyntax
import io.circe.syntax.EncoderOps
import org.http4s.client.dsl.io._
import org.http4s.headers._
import org.typelevel.ci.CIStringSyntax

object Server extends Serde {

  def tubiService(client: Client[IO])(implicit databaseContract: DatabaseContract, logger: Logger[IO]): Kleisli[IO, Request[IO], Response[IO]] = HttpRoutes.of[IO] {
    case GET -> Root / "tubi" =>
      val request = GET(
        uri"http://mock-content.interview.staging.sandbox.tubi.io/api/content/all?size=100&type=movie",
        Header.Raw(ci"x-api-key", "1bc682bd-0d0d-4c34-8c02-684ad7cd8bf9"),
        Accept(MediaType.application.json)
      )

      val result = client.expect[String](request).flatMap { response =>
        parser.parse(response).getOrElse(Json.Null).as[ContentResponse] match {
          case Left(value) => InternalServerError(s"Failed to parse response into class: $response Error: $value")
          case Right(value) => Ok(value.items.asJson.noSpaces)
        }
      }

      result

  }.orNotFound
}
