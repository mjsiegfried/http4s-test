import DataModel.{Content, ContentResponse}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

trait Serde {

  implicit val ContentDecoder: Decoder[Content] = deriveDecoder[Content]
  implicit val ContentEncoder: Encoder[Content] = deriveEncoder[Content]
  
  implicit val ContentResponseDecoder: Decoder[ContentResponse] = deriveDecoder[ContentResponse]
  implicit val ContentResponseEncoder: Encoder[ContentResponse] = deriveEncoder[ContentResponse]


}
