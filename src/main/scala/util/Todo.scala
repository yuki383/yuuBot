package util

object Todo {
  case class TODO(todo: String, achieved: Boolean = false) {
    override def toString: String = {
      val TODO = todo
      val Achieved =
        if(achieved) achieved.toString + " " else achieved.toString
      s"$Achieved: $TODO"
    }

    def update: TODO = TODO(todo, !achieved)


    def equals(obj: TODO): Boolean = {
      todo == obj.todo
    }


    def isEmpty: Boolean = {
      todo.isEmpty
    }
  }
}
