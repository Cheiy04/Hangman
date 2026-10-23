package hangman.controller

import hangman.model.{GameState, HangmanLogic}
import hangman.view.ConsoleUI

// A class controler to initailies the flow of the game
class Controller(private val ui: ConsoleUI, private val word: String, private val maxAttempts: Int = 6) {
  private var state: GameState = HangmanLogic.startGame(word, maxAttempts)

  def currentState: GameState = state

  //Our controller starts the game and nests the play loop function
  def startGame(): Unit = {
    ui.showWelcomeMessage()
    playLoop()
  }

  private def playLoop(): Unit = {
    while (state.isPlaying) {
      ui.showState(state)

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
        state = HangmanLogic.guess(state, letter)
        if (state.isWon) {
          ui.showMessage(s"You won! The word was '${state.word}'.")
          return
        }
        if (state.isGameOver) {
          ui.showMessage(s"Game over! The word was '${state.word}'.")
          return
        }
      }
    }
  }
}
