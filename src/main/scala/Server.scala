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
import retry.{RetryDetails, RetryPolicies, retryingOnAllErrors}

import scala.concurrent.duration.DurationInt

object Server extends TubiApi {

  def tubiService()(implicit logger: Logger[IO], client: Client[IO]): Kleisli[IO, Request[IO], Response[IO]] = HttpRoutes.of[IO] {
    case GET -> Root / "tubi" =>
     fetchAllContent()

    case GET -> Root / "genre" =>
     fetchContentByGenre()

  }.orNotFound
}
