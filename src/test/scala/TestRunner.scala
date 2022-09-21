import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits.toTraverseOps
import com.comcast.ip4s.IpLiteralSyntax
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object TestRunner extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {

    implicit val logger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]

    (for {
      client <- EmberClientBuilder.default[IO].build
      server <- EmberServerBuilder
        .default[IO]
        .withHost(ipv4"0.0.0.0")
        .withPort(port"8080")
        .withHttpApp(Server.tubiService(client))
        .build
    } yield (client, server)).use { case (client, server) =>

      val allTestSuites = List(
        SerdeTest.tests(),
        HttpTest.tests(client)
      )

      allTestSuites
        .sequence
        .map(_
          .map(testSuite => {
            System.out.println(s"Test suite: ${testSuite.name}")
            testSuite.results.foreach(test => {
              System.out.println(s"  ${test.name} : ${test.result}")
            })
          }))
        .value
        .map(_ => ExitCode(0))
    }
  }

}
