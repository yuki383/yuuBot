package util

import parser.TodoParser._
import util.Todo._

import scala.io.Source
import scala.util.Random

object Commands {

  case class yuuBot(commands: List[Command])

  sealed trait Command {
    def exec(input: String): Boolean
  }

  case class ReplyCommand(kind: String, regex: String,
                          replyList: List[String]) extends Command {
    override def exec(input: String): Boolean = {
      regex.r.findFirstIn(input) match {
        case Some(_) =>
          println(Random.shuffle(replyList).head)
          true
        case None => false
      }
    }
  }


  case class TODOCommand(kind: String, regex: String,
                         filename: String) extends Command {
    override def exec(input: String): Boolean = {
      regex.r.findPrefixOf(input) match {
        case Some(str) =>
          input.substring(str.length).trim match {
            case c if c.startsWith("list") =>
              todoList

            case c if c.startsWith("add") =>
              todoAdd(c)

            case c if c.startsWith("update") =>
              todoUpdate(c)

            case c if c.startsWith("delete") =>
              todoDelete(c)

            case _ =>
              false
          }
        case _ => false
      }
    }

    private def todoList: Boolean = {
      val todo = todoParse(filename)
      todo match {
        case Some(list: List[TODO]) if list.nonEmpty =>
          list.foreach{t => println(t.toString)}
        case Some(_) =>
          println("TODOリストは空です。")
        case None =>
          println("TODO.jsonファイルが見つかりません。")
      }
      true
    }

    private def todoAdd(str: String): Boolean = {
      val s = str.substring("add".length).trim
      val Todo = todoParse(filename)
      val beAdded = TODO(s)
      if (beAdded.isEmpty)
        println(println("TODOを入力してください。: \n    todo add <TODO>"))
      else
        TodoProcess(Todo){ list =>
          if (!todoContains(list, beAdded.todo)) {
            fileWrite(list :+ beAdded, filename)
            println(s"add: [${beAdded.toString}]")
          } else
            println(s"[${beAdded.todo}] は既に存在しています。\n")
        }

      true
    }

    private def todoUpdate(str: String): Boolean = {
      val s = str.substring("update".length).trim
      val Todo = todoParse(filename)
      TodoProcess(Todo){ list =>
        if (todoContains(list, s)) {
          val updateList = for (
            lst <- list
          ) yield if (lst.todo == s) lst.update else lst
          fileWrite(updateList, filename)
          println(s"update $s")
        } else {
          println(s"${s}は存在しません。")
        }
      }

      true
    }

    private def todoDelete(str: String): Boolean = {
      val s = str.substring("delete".length).trim
      val Todo = todoParse(filename)
      TodoProcess(Todo){ list =>
        if (todoContains(list, s)) {
          val deletedList = list.filterNot{ t => t.todo == s}
          fileWrite(deletedList, filename)
          println(s"delete $s")
        } else {
          println(s"${s}は存在しません。")
        }
      }

      true
    }

  }

  def todoParse(filename: String): Option[List[TODO]] = {
    val file = Source.fromFile(filename).mkString
    if (file.nonEmpty) JsonToTODO(file)
    else JsonToTODO("[]")
  }

  def todoContains(list: List[TODO], elem: String): Boolean = {
    def containsRec(lst: List[TODO]): Boolean = {
      if (lst.isEmpty) return false
      else if(lst.head.todo == elem) return true
      containsRec(lst.tail)
    }

    if (list.nonEmpty) containsRec(list)
    else false
  }

  def fileWrite(lst: List[TODO], filename: String): Unit = {
    val osw = outPutStreamWriter(filename)
    write(shaping(lst), osw)
  }

  def TodoProcess(opt: Option[List[TODO]])
                 (f: List[TODO] => Unit): Unit = {
    opt match {
      case Some(list) =>
        f(list)
      case None =>
        println("TODO.jsonファイルが見つかりません。")
    }
  }

}
