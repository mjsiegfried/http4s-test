import DatabaseContract.Country
import TestModel._
import cats.data.EitherT
import cats.effect.IO
import cats.implicits._
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, Json, parser}

object SerdeTest extends Serde {

  def testParsingSymmetry[T](obj: T)(implicit e: Encoder[T], d: Decoder[T]): TestResult = {
    parser.parse(obj.asJson.noSpaces).getOrElse(Json.Null).as[T] match {
      case Left(value) => TestFailed(value.getMessage())
      case Right(value) => if (value == obj) TestPass() else TestFailed(s"Parsed object: $value did not match original object: $obj ")
    }
  }

  val testCountryIsSymmetrical: EitherT[IO, TestError, Test] = {
    val testCountry: Country = Country("TEST", "Test Land", 4, Some(10.5))
    EitherT(Test("Test Country Serde is symmetrical", testParsingSymmetry(testCountry)).asRight[TestError].pure[IO])
  }

  def tests(): EitherT[IO, TestError, TestSuite] = {
    List(
      testCountryIsSymmetrical
    ).sequence.map(tests => TestSuite("SerdeTest", tests))

  }
}
