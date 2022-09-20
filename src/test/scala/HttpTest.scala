import TestModel.{Test, TestError, TestSuite}
import cats.data.EitherT
import cats.effect.IO
import cats.implicits.toTraverseOps
import org.http4s.client.Client
import org.http4s.implicits.http4sLiteralsSyntax

object HttpTest {

//  def test1(implicit client: Client[IO]): EitherT[IO, TestError, Test] = {
//    val target = uri"http://localhost:8080/hello/" / "Bob"
//    val result = client.expect[String](target)
//    result
//  }

  def tests(implicit client: Client[IO]): EitherT[IO, TestError, TestSuite] = {
    List(
     SerdeTest.testCountryIsSymmetrical
    ).sequence.map(tests => TestSuite("HttpTest", tests))

  }
}
