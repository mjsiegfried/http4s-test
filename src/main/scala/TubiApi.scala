import DataModel.ContentResponse
import cats.effect.IO
import io.circe.{Json, parser}
import org.http4s.{Header, MediaType, Response}
import org.http4s.dsl.io.{GET, InternalServerError, Ok}
import org.http4s.headers.Accept
import retry.{RetryDetails, RetryPolicies, retryingOnAllErrors}
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

trait TubiApi extends Serde {

  def fetchContentByGenre()(implicit logger: Logger[IO], client: Client[IO]): IO[Response[IO]] = {
    val request = GET(
      uri"http://mock-content.interview.staging.sandbox.tubi.io/api/content/genre/action",
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
  }

  def fetchAllContent()(implicit logger: Logger[IO], client: Client[IO]): IO[Response[IO]] = {
    val request = GET(
      uri"http://mock-content.interview.staging.sandbox.tubi.io/api/content/all?page=0&size=100&type=movie",
      Header.Raw(ci"x-api-key", "1bc682bd-0d0d-4c34-8c02-684ad7cd8bf9"),
      Accept(MediaType.application.json)
    )

    val result = client.expect[String](request).flatMap { response =>
      parser.parse(response).getOrElse(Json.Null).as[ContentResponse] match {
        case Left(value) => InternalServerError(s"Failed to parse response into class: $response Error: $value")
        case Right(value) => Ok(value.items.asJson.noSpaces)
      }
    }
    retryingOnAllErrors[Response[IO]](
      policy = RetryPolicies.limitRetriesByCumulativeDelay(6.seconds, RetryPolicies.constantDelay[IO](2.seconds)),
      onError = (err: Throwable, details: RetryDetails) => logger.info(s"Retrying request due to $err, Details: $details...")
    )(result)
  }
}
