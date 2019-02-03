import parser.CommandParser._
import util.Commands._

import scala.io.Source


object yuuBotMain extends App {
  val config = Source.fromFile("BotConfig.json").mkString
  val yuuBot = JsonToBot(config)  match {
    case Some(result) => result
    case None => scala.sys.error("BotConfig.jsonが壊れています。")
  }

  println("起動しました。")

  def checkInput(): Unit = {
    val input = scala.io.StdIn.readLine("yuuBot> ")
    if(input.startsWith("exit")) System.exit(0)

    def execute(input: String, commands: List[Command]): Unit = {
      if(commands.nonEmpty && !commands.head.exec(input)) {
        execute(input, commands.tail)
      }
    }
    execute(input, yuuBot.commands)
    checkInput()
  }
  checkInput()
}
