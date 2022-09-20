import TestModel.{Test, TestError, TestPass, TestResult, TestSuite}
import cats.data.EitherT
import cats.effect.IO
import cats.implicits.{catsSyntaxApplicativeId, catsSyntaxEitherId}

object SerdeTest {

  val test1: Test = Test("test1", TestPass())


  def tests(): EitherT[IO, TestError, TestSuite] = {
    EitherT(TestSuite("SerdeTest", List(
      test1
    )).asRight[TestError].pure[IO])
  }
}
