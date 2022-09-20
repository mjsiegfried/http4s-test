import cats.data.Kleisli
import cats.effect._
import com.comcast.ip4s.IpLiteralSyntax
import doobie.Transactor
import doobie.util.transactor.Transactor.Aux
import io.circe.Decoder
import org.http4s._
import org.http4s.client.Client
import org.http4s.dsl.io._
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits.http4sLiteralsSyntax
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.{Logger, SelfAwareStructuredLogger}
import io.circe.syntax.EncoderOps
import io.circe.generic.auto._

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
        .withHttpApp(Server.helloWorldService(client))
        .build
        .use(_ => IO.never)
        .as(ExitCode.Success)
    }
}