import DatabaseContract.Country
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

trait Serde {

  implicit val countryDecoder: Decoder[Country] = deriveDecoder[Country]
  implicit val countryEncoder: Encoder[Country] = deriveEncoder[Country]

}
