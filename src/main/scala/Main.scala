import cats.effect._
import com.comcast.ip4s.IpLiteralSyntax
import doobie.Transactor
import doobie.util.transactor.Transactor.Aux
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Main extends IOApp with Serde {

  def run(args: List[String]): IO[ExitCode] =
    EmberClientBuilder.default[IO].build.use { client =>

      implicit val logger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]
      implicit val xa: Aux[IO, Unit] = Transactor.fromDriverManager[IO](
        "org.postgresql.Driver", // driver classname
        "jdbc:postgresql:world", // connect URL (driver-specific)
        "postgres", // user
        "postgres" // password
      )

      implicit val dbContract: DatabaseContract = DatabaseContractImpl.jdbcDatabaseContract

      EmberServerBuilder
        .default[IO]
        .withHost(ipv4"0.0.0.0")
        .withPort(port"8080")
        .withHttpApp(Server.tubiService(client))
        .build
        .use(_ => IO.never)
        .as(ExitCode.Success)
    }
}