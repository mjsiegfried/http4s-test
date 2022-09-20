import DatabaseContract.{Country, DatabaseError}
import cats.data.EitherT
import cats.effect.IO
import cats.implicits.catsSyntaxEitherId
import doobie.implicits._
import doobie.util.transactor.Transactor
import org.typelevel.log4cats.Logger

object DatabaseContractImpl {

  def jdbcDatabaseContract(implicit xa: Transactor.Aux[IO, Unit], logger: Logger[IO]) = new DatabaseContract {
    override def getCountry(): EitherT[IO, DatabaseError, Country] = {

      val query = sql"select code, name, population, gnp from country"
        .query[Country]
        .to[List]

      EitherT(
        query
          .transact(xa)
          .map(_.headOption.map(_.asRight[DatabaseError])
            .getOrElse(DatabaseError("Country not found").asLeft[Country]))
          .handleErrorWith(err => logger.error(err.toString)
            .map(_ => DatabaseError(s"Database error $err").asLeft[Country])))

    }
  }
}
