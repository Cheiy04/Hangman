package hangman.controller

import hangman.model.HangmanModel
import hangman.view.ConsoleUI

class Controller(private val ui: ConsoleUI, private val word: String, private val maxAttempts: Int = 6) {
  private val model = new HangmanModel
  model.addObserver(ui)

  def startGame(): Unit = {
    ui.showWelcomeMessage()
    model.startGame(word, maxAttempts)
    playLoop()
  }

  private def playLoop(): Unit = {
    while (model.currentState.isPlaying) {
      val input = ui.askForGuess()
      if (input.equalsIgnoreCase("quit")) {
        ui.showMessage("Game ended by player.")
        return
      }
      
      // checking if value entered is valid
      val letter = input.headOption.getOrElse(' ')
      if (!letter.isLetter) {
        ui.showMessage("Please enter a valid letter.")
      } else {
        val state = model.guess(letter)
        if state.isWon then
          ui.showMessage(s"You won! The word was '${state.word}'.")
          return
        if state.isGameOver then
          ui.showMessage(s"Game over! The word was '${state.word}'.")
          return
      }
    }
  }
}
