package parser

import util.Commands._

import play.api.libs.json._

object CommandParser extends Parser {

  private object Formatter {
    implicit val TODOCommandFormat: OFormat[TODOCommand] = Json.format[TODOCommand]
    implicit val ReplyCommandFormat: OFormat[ReplyCommand] = Json.format[ReplyCommand]

    implicit val CommandFormat: Format[Command] = new Format[Command] {
      override def reads(json: JsValue): JsResult[Command] = json match {
        case o: JsObject =>
          o.fields.head._2 match {
            case JsString("TODO") =>
              o.validate[TODOCommand]
            case JsString("Reply") =>
              o.validate[ReplyCommand]
            case _ =>
              JsError()
          }
        case _ => JsError()
      }

      override def writes(o: Command): JsValue = o match {
        case todo: TODOCommand =>
          Json.obj(
            "kind" -> todo.kind,
            "regex" -> todo.regex,
            "filename" -> todo.filename
          )
        case reply: ReplyCommand =>
          Json.obj(
            "kind" -> reply.kind,
            "regex" -> reply.regex,
            "replyList" -> reply.replyList
          )
        case _ =>
          JsNull
      }
    }
  }

  def JsonToBot(input: String): Option[yuuBot] = {
    import Formatter._
    val commands = toOpt[List[Command]](input)
    commands match {
      case Some(list) =>
        Some(yuuBot(list))
      case _ =>
        None
    }

  }

}
