package hangman

import hangman.controller.Controller
import hangman.model.HangmanModel
import hangman.view.ConsoleUI

object Hangman {
  def main(args: Array[String]): Unit = {
    val word = args.headOption.getOrElse("hangman")
    val model = new HangmanModel
    val controller = new Controller(model, word)
    val ui = new ConsoleUI(controller.handleInput)
    model.addObserver(ui)
    controller.startGame()
    ui.startInput()
  }
}
