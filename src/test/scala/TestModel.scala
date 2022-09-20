object TestModel {

 sealed trait TestResult

 case class TestPass() extends TestResult
 case class TestFailed(msg: String) extends TestResult

 case class Test(name: String, result: TestResult)

 case class TestSuite(name: String, results: List[Test])
 case class TestError(msg: String)
}
