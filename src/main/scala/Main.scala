import cats.effect._
import com.comcast.ip4s.IpLiteralSyntax
import org.http4s._
import org.http4s.client.Client
import org.http4s.dsl.io._
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits.http4sLiteralsSyntax

object Main extends IOApp {


  def helloWorldService(client: Client[IO]) = HttpRoutes.of[IO] {
    case GET -> Root =>
      val target = uri"http://localhost:8080/hello/" / "Bob"
      Ok(client.expect[String](target))
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