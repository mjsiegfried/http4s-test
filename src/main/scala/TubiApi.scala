
import DataModel.{ContentResponse, SortedContentResponse, TubiError, UnknownTubiError, Video}
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

  def fetchContentByPage(page: Int, contentType: String)(implicit logger: Logger[IO], client: Client[IO]): EitherT[IO, TubiError, ContentResponse] = {
    val request = GET(
      //todo -- clean up this uri parsing
      Uri.fromString(s"http://mock-content.interview.staging.sandbox.tubi.io/api/content/all?page=$page&size=100&type=$contentType").getOrElse(uri""),
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

  def fetchContentSortedByTag(page: Int, contentType: String)(implicit logger: Logger[IO], client: Client[IO]): EitherT[IO, TubiError, SortedContentResponse] = {
    fetchContentByPage(page, contentType).map(contentResponse => {
      sortContentByFirstTag(contentResponse.items)
    })
  }


  // business logic
  def sortContentByTag(list: List[Video]): SortedContentResponse = {
    // create list of all tag -> videos
    val sortedValues = list.foldLeft(List.empty[(String, Video)])((acc, video) => {
      acc ++ video.tags.map(tag => (tag -> video))

    })
      // use groupBy to combine tags
      .groupBy((_._1)).map { case (tag, values) => tag -> values.map(_._2) }

    SortedContentResponse(sortedValues)
  }

  def sortContentByFirstTag(list: List[Video]): SortedContentResponse = {
    val sortedValues = list.foldLeft(Map.empty[String, List[Video]])((acc, video) => {

      // get first tag for video, get the accumulator's value for that tag, combine the list and reinsert the key
      video.tags.headOption.map { firstTag =>
        acc ++ Map(firstTag -> (acc.getOrElse(firstTag, List.empty) ::: List(video)))
      }.getOrElse(acc)

    })

    SortedContentResponse(sortedValues)
  }
}
