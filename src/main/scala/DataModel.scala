object DataModel {

  case class ContentResponse(count: Int, items: List[Video])

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
                  )

  case class SortedContentResponse(content: Map[String, List[Video]])
  case class SortedContent(tag: String, items: List[Video])

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
