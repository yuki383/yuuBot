package parser

import play.api.libs.json._

trait Parser {

  def toOpt[T](input: String)(implicit rds: Reads[T]): Option[T] = {
    try {
      Option(rds.reads(Json.parse(input)).get)
    } catch {
      case _: Exception =>
        None
    }
  }

}
