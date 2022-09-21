
import DataModel.{ContentResponse, TubiError, UnknownTubiError}
import cats.data.EitherT
import cats.effect.IO
import cats.implicits.catsSyntaxEitherId
import io.circe.{Json, parser}
import org.http4s.client.Client
import org.http4s.client.dsl.io._
import org.http4s.dsl.io._
import org.http4s.headers.Accept
import org.http4s.implicits.http4sLiteralsSyntax
import org.http4s.{Header, MediaType, Uri}
import org.typelevel.ci.CIStringSyntax
import org.typelevel.log4cats.Logger

trait TubiApi extends Serde {

  def fetchContentByPage(page: Int)(implicit logger: Logger[IO], client: Client[IO]): EitherT[IO, TubiError, ContentResponse] = {
    val request = GET(
      Uri.fromString(s"http://mock-content.interview.staging.sandbox.tubi.io/api/content/all?page=$page&size=100&type=movie").getOrElse(uri""),
      Header.Raw(ci"x-api-key", "1bc682bd-0d0d-4c34-8c02-684ad7cd8bf9"),
      Accept(MediaType.application.json)
    )

    EitherT(client.expect[String](request).map { response =>
      parser.parse(response).getOrElse(Json.Null).as[ContentResponse] match {
        case Left(value) => UnknownTubiError(s"Failed to parse response: $response, error: $value").asLeft[ContentResponse]
        case Right(value) => value.asRight[TubiError]
      }
    })
  }

  def fetchAllContent()(implicit logger: Logger[IO], client: Client[IO]): EitherT[IO, TubiError, ContentResponse] = {
    val request = GET(
      uri"http://mock-content.interview.staging.sandbox.tubi.io/api/content/all?page=0&size=100&type=movie",
      Header.Raw(ci"x-api-key", "1bc682bd-0d0d-4c34-8c02-684ad7cd8bf9"),
      Accept(MediaType.application.json)
    )

    EitherT(client.expect[String](request).map { response =>
      parser.parse(response).getOrElse(Json.Null).as[ContentResponse] match {
        case Left(value) => UnknownTubiError(s"Failed to parse response: $response, error: $value").asLeft[ContentResponse]
        case Right(value) => value.asRight[TubiError]
      }
    })
  }
}
