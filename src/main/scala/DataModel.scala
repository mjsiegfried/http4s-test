object DataModel {

  case class ContentResponse(count: Int, items: List[Content])

  sealed trait Content {
    def tags: List[String]
  }

  case class Video(
                    description: String,
                    duration_ms: Long,
                    genre: String,
                    id: Int,
                    images: List[String],
                    language: String,
                    policy: String,
                    rating: String,
                    release_year: Int,
                    tags: List[String],
                    title: String,
                    `type`: String
                  ) extends Content

  case class Series(
                     id: Int,
                     description: String,
                     genre: String,
                     language: String,
                     title: String,
                     `type`: String,
                     score: Option[Score],
                     policy: String,
                     rating: String,
                     release_year: Int,
                     tags: List[String],
                     seasons: Option[List[Season]],
                   ) extends Content

  case class Season(
                     id: String,
                     number: Int,
                     release_year: Int,
                     tags: List[String],
                     `type`: String,
                     episodes: List[Video]
                   )

  case class Score(imdb: String, rotten_tomatoes: String)

  case class SortedContentResponse(content: Map[String, List[Content]])

  case class SortedContent(tag: String, items: List[Content])

  sealed trait TubiError {
    def msg: String
  }

  case class UnknownTubiError(msg: String) extends TubiError


  // todo -- ???
  final val genres = List(
    "action",
    "adventure",
    "animation",
    "anime",
    "biography",
    "comedy",
    "crime",
    "documentary",
    "drama",
    "family",
    "fantasy",
    "foreign",
    "history",
    "holiday",
    "horror",
    "independent",
    "lifestyle",
    "music",
    "musical",
    "mystery",
    "news",
    "reality",
    "romance",
    "sci-fi",
    "short",
    "sport",
    "talk_show",
    "thriller",
    "war",
    "western",
    "science",
    "entertainment"
  )

}
