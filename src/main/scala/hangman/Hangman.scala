package hangman

import hangman.controller.Controller
import hangman.model.HangmanModel
import hangman.view.ConsoleUI

object Hangman {
  def main(args: Array[String]): Unit = {
    val word = args.headOption.getOrElse("hangman")
    val controller = new Controller(new ConsoleUI(), new HangmanModel, word)
    controller.startGame()
  }
}
