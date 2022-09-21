import TestModel.{Test, TestError, TestFailed, TestPass, TestResult, TestSuite}
import cats.data.EitherT
import cats.effect.IO
import cats.implicits.{catsSyntaxEitherId, toTraverseOps}
import org.http4s.client.Client
import org.http4s.implicits.http4sLiteralsSyntax

object HttpTest {

  def testHelloEndpoint(implicit client: Client[IO]): EitherT[IO, TestError, Test] = {
    val target = uri"http://localhost:8080/tubi/"
    val result = client.expect[String](target)
    val expectedValue = "Hello, Bob."

    EitherT(result.attempt.map {
      case Left(value) => TestFailed(s"Http Error: $value")
      case Right(value) => if (value == expectedValue) {
        TestPass()
      }
      else {
        TestFailed(s"Returned value: $value did not match expected value: $expectedValue")
      }
    }.map(result => Test("Test Hello Endpoint", result).asRight[TestError]))

  }

  def tests(implicit client: Client[IO]): EitherT[IO, TestError, TestSuite] = {
    List(
      testHelloEndpoint
    ).sequence.map(tests => TestSuite("HttpTest", tests))

  }
}
