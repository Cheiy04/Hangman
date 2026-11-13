package hangman.controller

import hangman.common.GameInput
import hangman.model.GameModel

class Controller(
    private val model: GameModel,
    private val word: String,
    private val maxAttempts: Int = 6
) {
  def startGame(): Unit = {
    model.startGame(word, maxAttempts)
  }

  def handleInput(input: GameInput): Boolean = {
    input match {
      case GameInput.Guess(letter) =>
        model.guess(letter)
        model.currentState.isPlaying

      case GameInput.Quit =>
        model.quit()
        false

      case GameInput.Invalid =>
        model.invalidInput()
        model.currentState.isPlaying
    }
  }
}
