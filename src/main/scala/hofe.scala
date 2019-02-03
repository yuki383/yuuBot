import play.api.libs.json._

import scala.io.Source

object hofe extends App {
  trait Animal
  case class Cat(name: String, kind: String = "cat") extends Animal
  case object Failure extends Animal
  object Cat {
    implicit val CatFormat = Json.format[Cat]
  }
  implicit val AnimalFormat = new Format[Animal] {
    override def writes(o: Animal): JsValue = o match {
      case c: Cat =>
        Json.obj("name" -> c.name, "kind" -> c.kind)
    }

    override def reads(json: JsValue): JsResult[Animal] = json match {
      case o: JsObject =>
        o.fields(1)._2 match {
          case JsString("cat") =>
            o.validate[Cat]
        }
    }
  }

  val json =
    """{ "name": "tama", "kind": "cat"}"""
  val c = Json.parse(json).validate[Animal].getOrElse(Failure)

  println(c)
}