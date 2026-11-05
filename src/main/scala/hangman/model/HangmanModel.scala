package hangman.model

import hangman.observer.Observable

class HangmanModel extends Observable[GameState] {
  private var state: Option[GameState] = None

  def currentState: GameState =
    state.getOrElse(throw new IllegalStateException("The game has not been started."))

  def startGame(word: String, maxGuesses: Int = 6): GameState = {
    val newState = GameState(
      word = word,
      guessedWords = Set.empty,
      attemptsLeft = maxGuesses,
      maxAttempts = maxGuesses
    )
    state = Some(newState)
    notifyObservers(newState)
    newState
  }

  def guess(letter: Char): GameState = {
    val previousState = currentState
    val newState = previousState.guess(letter)
    state = Some(newState)
    if newState != previousState then notifyObservers(newState)
    newState
  }

}
