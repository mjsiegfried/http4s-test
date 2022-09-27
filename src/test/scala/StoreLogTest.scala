import TestModel.{Test, TestError, TestFailed, TestPass, TestResult, TestSuite}
import cats.data.EitherT
import cats.effect.IO
import cats.implicits.{catsSyntaxApplicativeId, catsSyntaxEitherId, toTraverseOps}

object StoreLogTest {

  val testStoreLog1: EitherT[IO, TestError, Test] = {

    val penalty = Example.computePenalty("Y Y N Y", 0)
    val expectedPenalty = 3

    val testResult: TestResult = if (penalty.==(expectedPenalty)) {
      TestPass()
    } else {
      TestFailed(s"Penalty is wrong $penalty, expected $expectedPenalty")
    }

    EitherT(Test("Test store log penalty is correct", testResult).asRight[TestError].pure[IO])
  }

  val testStoreLog2: EitherT[IO, TestError, Test] = {

    val penalty = Example.computePenalty("N Y N Y", 2)

    val expectedPenalty = 2

    val testResult: TestResult = if (penalty.==(expectedPenalty)) {
      TestPass()
    } else {
      TestFailed(s"Penalty is wrong $penalty, expected $expectedPenalty")
    }

    EitherT(Test("Test store log penalty is correct", testResult).asRight[TestError].pure[IO])
  }


  val testStoreLog3: EitherT[IO, TestError, Test] = {

    val penalty = Example.computePenalty("Y Y N Y", 4)
    val expectedPenalty = 1
    val testResult: TestResult = if (penalty.==(expectedPenalty)) {
      TestPass()
    } else {
      TestFailed(s"Penalty is wrong $penalty, expected $expectedPenalty")
    }

    EitherT(Test("Test store log penalty is correct", testResult).asRight[TestError].pure[IO])
  }

  val testStoreLog4: EitherT[IO, TestError, Test] = {

    val penalty = Example.computePenalty("Y Y Y N N N N", 0)
    val expectedPenalty = 3
    val testResult: TestResult = if (penalty.==(expectedPenalty)) {
      TestPass()
    } else {
      TestFailed(s"Penalty is wrong $penalty, expected $expectedPenalty")
    }

    EitherT(Test("Test store log penalty is correct", testResult).asRight[TestError].pure[IO])
  }


  val testStoreLog5: EitherT[IO, TestError, Test] = {

    val penalty = Example.computePenalty("Y Y Y N N N N", 7)
    val expectedPenalty = 4
    val testResult: TestResult = if (penalty.==(expectedPenalty)) {
      TestPass()
    } else {
      TestFailed(s"Penalty is wrong $penalty, expected $expectedPenalty")
    }

    EitherT(Test("Test store log penalty is correct", testResult).asRight[TestError].pure[IO])
  }



  val testStoreLog6: EitherT[IO, TestError, Test] = {

    val penalty = Example.computePenalty("Y Y Y N N N N", 3)
    val expectedPenalty = 0
    val testResult: TestResult = if (penalty.==(expectedPenalty)) {
      TestPass()
    } else {
      TestFailed(s"Penalty is wrong $penalty, expected $expectedPenalty")
    }

    EitherT(Test("Test store log penalty is correct", testResult).asRight[TestError].pure[IO])
  }

  val testStoreLog7: EitherT[IO, TestError, Test] = {

    val penalty = Example.computePenalty("", 0)
    val expectedPenalty = 0
    val testResult: TestResult = if (penalty.==(expectedPenalty)) {
      TestPass()
    } else {
      TestFailed(s"Penalty is wrong $penalty, expected $expectedPenalty")
    }

    EitherT(Test("Test store log penalty is correct", testResult).asRight[TestError].pure[IO])
  }

  val testStoreLog8: EitherT[IO, TestError, Test] = {

    val penalty = Example.computePenalty("Y N Y N N N N", 3)
    val expectedPenalty = 1
    val testResult: TestResult = if (penalty.==(expectedPenalty)) {
      TestPass()
    } else {
      TestFailed(s"Penalty is wrong $penalty, expected $expectedPenalty")
    }

    EitherT(Test("Test store log penalty is correct", testResult).asRight[TestError].pure[IO])
  }


  val testStoreLog9: EitherT[IO, TestError, Test] = {

    val penalty = Example.findBestClosingTime("Y Y N N")
    val expectedClosingTime = 2
    val testResult: TestResult = if (penalty.==(expectedClosingTime)) {
      TestPass()
    } else {
      TestFailed(s"Closing time is wrong $penalty, expected $expectedClosingTime")
    }

    EitherT(Test("Test store log closing time is correct", testResult).asRight[TestError].pure[IO])
  }

  def tests(): EitherT[IO, TestError, TestSuite] = {
    List(
      testStoreLog1,
      testStoreLog2,
      testStoreLog3,
      testStoreLog4,
      testStoreLog5,
      testStoreLog6,
      testStoreLog7,
      testStoreLog8,
      testStoreLog9
    ).sequence.map(tests => TestSuite("TestStoreLog", tests))

  }
}
