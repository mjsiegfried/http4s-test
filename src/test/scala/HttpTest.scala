import TestModel.{Test, TestError, TestFailed, TestPass, TestResult, TestSuite}
import cats.data.EitherT
import cats.effect.IO
import cats.implicits.{catsSyntaxEitherId, toTraverseOps}
import org.http4s.{Header, MediaType, Uri}
import org.http4s.client.Client
import org.http4s.headers.Accept
import org.http4s.implicits.http4sLiteralsSyntax
import org.typelevel.ci.CIStringSyntax
import org.http4s.client.dsl.io._
import org.http4s.dsl.io.GET
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
    }.map(result => Test("Test Movie Endpoint", result).asRight[TestError]))

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
    }.map(result => Test("Test Series Endpoint", result).asRight[TestError]))
  }


  def testStreamEndpoint(implicit client: Client[IO]): EitherT[IO, TestError, Test] = {

    val request = GET(uri"http://localhost:8080/tubi/stream")
    //todo
    EitherT(client.stream(request).flatMap(resp => {
      resp.body
    }).compile.drain.map(_ => Test("Stream Test", TestPass()).asRight[TestError]))

  }

  def tests(implicit client: Client[IO]): EitherT[IO, TestError, TestSuite] = {
    List(
      testMainEndpoint,
      testMovieEndpoint,
      testSeriesEndpoint,
      testStreamEndpoint
    ).sequence.map(tests => TestSuite("HttpTest", tests))

  }
}
