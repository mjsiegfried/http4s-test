import cats.effect.IO

trait DatabaseContract {
  def foo(): IO[String]

}
