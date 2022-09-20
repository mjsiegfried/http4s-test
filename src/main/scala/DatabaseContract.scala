import DatabaseContract.{Country, DatabaseError}
import cats.data.EitherT
import cats.effect.IO

trait DatabaseContract {
  def getCountry(): EitherT[IO, DatabaseError, Country]
}

object DatabaseContract {
  case class DatabaseError(msg: String)
  case class Country(code: String, name: String, pop: Int, gnp: Option[Double])
}
