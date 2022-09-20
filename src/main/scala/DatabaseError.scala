object DatabaseError {

  sealed trait DatabaseError {
    def msg: String
  }

  case class ObjectNotFoundError(msg: String) extends DatabaseError
  case class UnknownDatabaseError(msg: String) extends DatabaseError
}
