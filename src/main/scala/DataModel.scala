object DataModel {

  case class ContentResponse(count: Int, items: List[Content])

  case class Content(
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

}
