import cats.effect.{ExitCode, IO, IOApp}

object Example extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {

    IO.unit.map(_ => ExitCode(0))
  }

  def findBestClosingTime(storeLog: String): Int = {

    val customerPrescense = storeLog.split(" ").map(_.toUpperCase()).map {
      case "Y" => true
      case "N" => false
      case _ => false // todo -- error case
    }

   (1 to customerPrescense.length)
     .map { closingTime =>
      (computePenalty(storeLog, closingTime) -> closingTime)
    }
     .minBy(_._1)
     ._2

  }

  def computePenalty(storeLog: String, closingTime: Int): Int = {
    // todo -- assume store log is always as long as the closing time or longer, check for errors later

    val customerPrescense = storeLog.split(" ").map(_.toUpperCase()).map {
      case "Y" => true
      case "N" => false
      case _ => false // todo -- error case
    }

    customerPrescense.zipWithIndex.map {
      case (bool, hour) => (bool, hour + 1)
    }.foldLeft(0)((runningPenalty, tuple) => {
      val (wereCustomersThere, hour) = tuple

      if (hour > closingTime && wereCustomersThere) {
        runningPenalty + 1
      } else if (hour > closingTime && !wereCustomersThere) {
        runningPenalty
      } else if (hour <= closingTime && wereCustomersThere) {
        runningPenalty
      } else {
        runningPenalty + 1
      }
    })
  }
}
