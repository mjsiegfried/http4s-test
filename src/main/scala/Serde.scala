import DataModel.{Content, ContentResponse}
import DatabaseContract.Country
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import org.http4s.{EntityDecoder, Response}

trait Serde {

  implicit val countryDecoder: Decoder[Country] = deriveDecoder[Country]
  implicit val countryEncoder: Encoder[Country] = deriveEncoder[Country]

  implicit val ContentDecoder: Decoder[Content] = deriveDecoder[Content]
  implicit val ContentEncoder: Encoder[Content] = deriveEncoder[Content]
  
  implicit val ContentResponseDecoder: Decoder[ContentResponse] = deriveDecoder[ContentResponse]
  implicit val ContentResponseEncoder: Encoder[ContentResponse] = deriveEncoder[ContentResponse]


}
