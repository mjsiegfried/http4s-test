import DatabaseContract.Country
import SerdeTest.testCountry
import TestModel.{Test, TestError, TestFailed, TestPass, TestResult, TestSuite}
import cats.data.EitherT
import cats.effect.IO
import cats.implicits._
import io.circe.{Decoder, Encoder, Json, parser}
import io.circe.syntax.EncoderOps

object SerdeTest extends Serde {

  def testParsingSymmetry[T](obj: T)(implicit e: Encoder[T], d: Decoder[T]) = {
    parser.parse(obj.asJson.noSpaces).getOrElse(Json.Null).as[T] match {
      case Left(value) => TestFailed(value.getMessage())
      case Right(value) => if (value == obj) TestPass() else TestFailed(s"Parsed object: $value did not match original object: $obj ")
    }
  }

  val test1: EitherT[IO, TestError, Test] = EitherT(Test("test1", TestPass()).asRight[TestError].pure[IO])

  val testCountry = Country("TEST", "Test Land", 4, Some(10.5))
  val test2 = EitherT(Test("Test Country Serde is symmetrical", testParsingSymmetry(testCountry) ).asRight[TestError].pure[IO])

  def tests(): EitherT[IO, TestError, TestSuite] = {
    List(
      test1,
      test2
    ).sequence.map(tests => TestSuite("SerdeTest", tests))

  }
}
