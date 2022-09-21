import DataModel.{Content, ContentResponse, Score, Season, Series, SortedContent, SortedContentResponse, Video}
import cats.implicits.toFunctorOps
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder}

trait Serde {

  implicit val VideoDecoder: Decoder[Video] = deriveDecoder[Video]
  implicit val VideoEncoder: Encoder[Video] = deriveEncoder[Video]

  implicit val SeasonDecoder: Decoder[Season] = deriveDecoder[Season]
  implicit val SeasonEncoder: Encoder[Season] = deriveEncoder[Season]

  implicit val SeriesDecoder: Decoder[Series] = deriveDecoder[Series]
  implicit val SeriesEncoder: Encoder[Series] = deriveEncoder[Series]

  implicit val ScoreDecoder: Decoder[Score] = deriveDecoder[Score]
  implicit val ScoreEncoder: Encoder[Score] = deriveEncoder[Score]

  implicit val ContentDecoder: Decoder[Content] = List[Decoder[Content]](
    Decoder[Video].widen,
    Decoder[Series].widen
  ).reduceLeft(_ or _)

  implicit val ContentEncoder: Encoder[Content] = Encoder.instance {
    case v@Video(description, duration_ms, genre, id, images, language, policy, rating, release_year, tags, title, _) => v.asJson
    case s@Series(id, description, genre, language, title, _, score, policy, rating, release_year, tags, seasons) => s.asJson
  }

  implicit val ContentResponseDecoder: Decoder[ContentResponse] = deriveDecoder[ContentResponse]
  implicit val ContentResponseEncoder: Encoder[ContentResponse] = deriveEncoder[ContentResponse]

  implicit val SortedContentDecoder: Decoder[SortedContent] = deriveDecoder[SortedContent]
  implicit val SortedContentEncoder: Encoder[SortedContent] = deriveEncoder[SortedContent]

  implicit val SortedSortedContentResponseDecoder: Decoder[SortedContentResponse] = deriveDecoder[SortedContentResponse]
  implicit val SortedSortedContentResponseEncoder: Encoder[SortedContentResponse] = deriveEncoder[SortedContentResponse]

}