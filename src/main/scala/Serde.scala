import DataModel.{Video, ContentResponse}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

trait Serde {

  implicit val VideoDecoder: Decoder[Video] = deriveDecoder[Video]
  implicit val VideoEncoder: Encoder[Video] = deriveEncoder[Video]
  
  implicit val ContentResponseDecoder: Decoder[ContentResponse] = deriveDecoder[ContentResponse]
  implicit val ContentResponseEncoder: Encoder[ContentResponse] = deriveEncoder[ContentResponse]


}
