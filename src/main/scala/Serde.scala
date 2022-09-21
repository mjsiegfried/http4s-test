import DataModel.{ContentResponse, SortedContent, SortedContentResponse, Video}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

trait Serde {

  implicit val VideoDecoder: Decoder[Video] = deriveDecoder[Video]
  implicit val VideoEncoder: Encoder[Video] = deriveEncoder[Video]
  
  implicit val ContentResponseDecoder: Decoder[ContentResponse] = deriveDecoder[ContentResponse]
  implicit val ContentResponseEncoder: Encoder[ContentResponse] = deriveEncoder[ContentResponse]

  implicit val SortedContentDecoder: Decoder[SortedContent] = deriveDecoder[SortedContent]
  implicit val SortedContentEncoder: Encoder[SortedContent] = deriveEncoder[SortedContent]

  implicit val SortedSortedContentResponseDecoder: Decoder[SortedContentResponse] = deriveDecoder[SortedContentResponse]
  implicit val SortedSortedContentResponseEncoder: Encoder[SortedContentResponse] = deriveEncoder[SortedContentResponse]

}
