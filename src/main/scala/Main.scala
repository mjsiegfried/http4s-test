import cats.effect._
import cats.implicits.catsSyntaxApplicativeId
import com.comcast.ip4s.IpLiteralSyntax
import doobie.implicits._
import doobie.{ConnectionIO, Transactor}
import org.http4s._
import org.http4s.client.Client
import org.http4s.dsl.io._
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits.http4sLiteralsSyntax
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Main extends IOApp {

  val logger = Slf4jLogger.getLogger[IO]

  def helloWorldService(client: Client[IO]) = HttpRoutes.of[IO] {
    case GET -> Root =>
      val target = uri"http://localhost:8080/hello/" / "Bob"
      val result = client.expect[String](target)
      Ok(result)
    case GET -> Root / "db" =>
      val program1 = "42".pure[ConnectionIO]
      val xa = Transactor.fromDriverManager[IO](
        "org.postgresql.Driver", // driver classname
        "jdbc:postgresql:world", // connect URL (driver-specific)
        "postgres", // user
        "" // password
      )
      val result = program1
        .transact(xa)
        .handleErrorWith(err => logger.error(err.toString)
          .map(_ => "Err"))
      Ok(result)

    case GET -> Root / "hello" / name =>
      Ok(s"Hello, $name.")


  }.orNotFound

  def run(args: List[String]): IO[ExitCode] =
    EmberClientBuilder.default[IO].build.use { client =>
      EmberServerBuilder
        .default[IO]
        .withHost(ipv4"0.0.0.0")
        .withPort(port"8080")
        .withHttpApp(helloWorldService(client))
        .build
        .use(_ => IO.never)
        .as(ExitCode.Success)
    }
}