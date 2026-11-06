package hangman.controller

import hangman.model.HangmanModel
import hangman.view.ConsoleUI

class Controller(
    private val ui: ConsoleUI,
    private val model: HangmanModel,
    private val word: String,
    private val maxAttempts: Int = 6
) {
  model.addObserver(ui)

  def startGame(): Unit = {
    model.startGame(word, maxAttempts)
    playLoop()
  }

  private def playLoop(): Unit = {
    while (model.currentState.isPlaying) {
      val input = ui.askForGuess()
      if (input.equalsIgnoreCase("quit")) {
        model.quit()
        return
      }
      
      // checking if value entered is valid
      val letter = input.headOption.getOrElse(' ')
      if (!letter.isLetter) {
        model.invalidInput()
      } else {
        val state = model.guess(letter)
        if state.isWon then
          return
        if state.isGameOver then
          return
      }
    }
  }
}
