import DatabaseContract.Country
import TestModel.{Test, TestError, TestFailed, TestPass, TestResult, TestSuite}
import cats.data.EitherT
import cats.effect.IO
import cats.implicits._
import io.circe.{Json, parser}
import io.circe.syntax.EncoderOps

object SerdeTest extends Serde {

  val testCountry = Country("TEST", "Test Land", 4, Some(10.5))
  val x = parser.parse(testCountry.asJson.noSpaces).getOrElse(Json.Null).as[Country] match {
    case Left(value) => TestFailed(value.getMessage())
    case Right(value) => if(value == testCountry) TestPass() else TestFailed(s"Objects did not match $value : $testCountry")
  }

  val test1: EitherT[IO, TestError, Test] = EitherT(Test("test1", TestPass()).asRight[TestError].pure[IO])

  def tests(): EitherT[IO, TestError, TestSuite] = {
    List(
      test1
    ).sequence.map(tests => TestSuite("SerdeTest", tests))

  }
}
