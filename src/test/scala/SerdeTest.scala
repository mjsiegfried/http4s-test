import DataModel.Video
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

  val testContentIsSymmetrical: EitherT[IO, TestError, Test] = {
    val testContent: Video = Video(
      "", 1, "", 1, List.empty, "", "", "", 9, List.empty, "", ""
    )
    EitherT(Test("Test Content Serde is symmetrical", testParsingSymmetry(testContent)).asRight[TestError].pure[IO])
  }

  def tests(): EitherT[IO, TestError, TestSuite] = {
    List(
      testContentIsSymmetrical
    ).sequence.map(tests => TestSuite("SerdeTest", tests))

  }
}
