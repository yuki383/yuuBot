package parser

import util.Todo._

import play.api.libs.json._
import java.io.{FileOutputStream, OutputStreamWriter}

object TodoParser extends Parser {

  private object Formatter {
    implicit val TodoFormat: OFormat[TODO] = Json.format[TODO]
  }

  def JsonToTODO(input: String): Option[List[TODO]] = {
    import Formatter._
    val todo = toOpt[List[TODO]](input)
    todo match {
      case t@Some(_) =>
        t
      case _ =>
        None
    }
  }



  def outPutStreamWriter(filename: String): OutputStreamWriter = {
    val fos = new FileOutputStream(filename)
    new OutputStreamWriter(fos, "UTF-8")
  }



  def write(content: String, writer: OutputStreamWriter): Unit = {
    try {
      writer.write(content)
    } catch {
      case _: Exception => println("TODO.jsonが見つかりません。")
    } finally {
      writer.close()
    }
  }

  def shaping(todo: List[TODO]): String = {
    import Formatter._
    val json = Json.toJson(todo)
    Json.prettyPrint(json)
  }

}
