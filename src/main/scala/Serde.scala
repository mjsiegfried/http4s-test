import DatabaseContract.Country
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

trait Serde {

  implicit val countryDecoder: Decoder[Country] = deriveDecoder[Country]
  implicit val countryEncoder: Encoder[Country] = deriveEncoder[Country]

}
