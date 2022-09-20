import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits.toTraverseOps

object TestRunner extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {

    val allTestSuites = List(
      SerdeTest.tests()
    )

    allTestSuites
      .sequence
      .map(_
        .map(testSuite => {
          System.out.println(s"Test suite: ${testSuite.name}")
          testSuite.results.foreach(test => {
            System.out.println(s"  ${test.name} : ${test.result}")
          })
        }))
      .value
      .map(_ => ExitCode(0))
  }
}
