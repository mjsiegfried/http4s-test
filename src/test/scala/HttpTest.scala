import TestModel.{Test, TestError, TestFailed, TestPass, TestResult, TestSuite}
import cats.data.EitherT
import cats.effect.IO
import cats.implicits.{catsSyntaxEitherId, toTraverseOps}
import org.http4s.client.Client
import org.http4s.implicits.http4sLiteralsSyntax

object HttpTest {

  def testMainEndpoint(implicit client: Client[IO]): EitherT[IO, TestError, Test] = {
    val target = uri"http://localhost:8080/tubi"
    val result = client.expect[String](target)
    val expectedValue = "Hello, Max." //todo -- change this

    EitherT(result.attempt.map {
      case Left(value) => TestFailed(s"Http Error: $value")
      case Right(value) => if (value == expectedValue) {
        TestPass()
      }
      else {
        TestFailed(s"Returned value: $value did not match expected value: $expectedValue")
      }
    }.map(result => Test("Test Main Endpoint", result).asRight[TestError]))

  }


  def testMovieEndpoint(implicit client: Client[IO]): EitherT[IO, TestError, Test] = {
    val target = uri"http://localhost:8080/tubi/movie"
    val result = client.expect[String](target)
    val expectedValue = "Hello, Max." //todo -- change this

    EitherT(result.attempt.map {
      case Left(value) => TestFailed(s"Http Error: $value")
      case Right(value) => if (value == expectedValue) {
        TestPass()
      }
      else {
        TestFailed(s"Returned value: $value did not match expected value: $expectedValue")
      }
    }.map(result => Test("Test Main Endpoint", result).asRight[TestError]))

  }


  def testSeriesEndpoint(implicit client: Client[IO]): EitherT[IO, TestError, Test] = {
    val target = uri"http://localhost:8080/tubi/series"
    val result = client.expect[String](target)
    val expectedValue = "Hello, Max." //todo -- change this

    EitherT(result.attempt.map {
      case Left(value) => TestFailed(s"Http Error: $value")
      case Right(value) => if (value == expectedValue) {
        TestPass()
      }
      else {
        TestFailed(s"Returned value: $value did not match expected value: $expectedValue")
      }
    }.map(result => Test("Test Main Endpoint", result).asRight[TestError]))
  }


  def tests(implicit client: Client[IO]): EitherT[IO, TestError, TestSuite] = {
    List(
      testMainEndpoint,
    ).sequence.map(tests => TestSuite("HttpTest", tests))

  }
}
