import cats.effect.IO
import doobie.implicits._
import doobie.util.transactor.Transactor
import org.typelevel.log4cats.Logger

object DatabaseContractImpl {

  def jdbcDatabaseContract(implicit xa: Transactor.Aux[IO, Unit], logger: Logger[IO]) = new DatabaseContract {
    override def foo(): IO[String] = {
      val query = sql"select name from country"
        .query[String].to[List]

      query
        .transact(xa)
        .map(_.headOption.getOrElse("Nothing found."))
        .handleErrorWith(err => logger.error(err.toString)
          .map(_ => err.toString))

    }
  }
}
