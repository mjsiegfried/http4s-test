import cats.data.Kleisli
import cats.effect.IO
import cats.implicits.catsSyntaxApplicativeId
import io.circe.syntax.EncoderOps
import org.http4s.client.Client
import org.http4s.dsl.io.{->, /, GET, Root}
import org.http4s.{HttpRoutes, Request, Response}
import org.typelevel.log4cats.Logger
import retry.{RetryDetails, RetryPolicies, retryingOnAllErrors}
import org.http4s.dsl.io._

import scala.concurrent.duration.DurationInt

object Server extends TubiApi {

  def tubiService()(implicit logger: Logger[IO], client: Client[IO]): Kleisli[IO, Request[IO], Response[IO]] = HttpRoutes.of[IO] {
    case GET -> Root / "tubi" =>

      val apiResult = fetchAllContent()
        .map(contentResponse => Ok(contentResponse.asJson.noSpaces.pure[IO]))
        .leftMap(error => Ok(error.msg.pure[IO]))
        .merge
        .flatten

      retryingOnAllErrors[Response[IO]](
        policy = RetryPolicies.limitRetriesByCumulativeDelay(6.seconds, RetryPolicies.constantDelay[IO](2.seconds)),
        onError = (err: Throwable, details: RetryDetails) => logger.info(s"Retrying request due to $err, Details: $details...")
      )(apiResult)


  }.orNotFound
}
