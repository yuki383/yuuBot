/*

import org.scalatest._
import parser.CommandParser
import util.Commands._
class CommandParserSpec extends FlatSpec with DiagrammedAssertions {

  "JsonToBot関数" should "Json形式のStringをOption[yuuBot]に変換できる" in {
    def json(kind: String):String =
      s"""
        |[
        |  {
        |    "kind": "$kind",
        |    "regex": "todo",
        |    "filename": "TODO.json"
        |  }
        |]
      """.stripMargin
    val bot = yuuBot(List(TODOCommand("TODO", "todo", "TODO.json")))
    assert(CommandParser.JsonToBot(json("TODO")).contains(bot))
    assert(CommandParser.JsonToBot(json("empty")).isEmpty)
    assert(CommandParser.JsonToBot("").isEmpty)
  }

}

*/
