package hangman.model

import hangman.observer.Observable

class HangmanModel extends Observable[GameUpdate] {
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
    notifyObservers(GameUpdate(newState, Some("Welcome to Hangman!\nGuess one letter at a time. Type 'quit' to exit.")))
    newState
  }

  def guess(letter: Char): GameState = {
    val previousState = currentState
    val newState = previousState.guess(letter)
    state = Some(newState)
    if newState != previousState then
      val message =
        if newState.isWon then Some(s"You won! The word was '${newState.word}'.")
        else if newState.isGameOver then Some(s"Game over! The word was '${newState.word}'.")
        else None
      notifyObservers(GameUpdate(newState, message))
    newState
  }

  def invalidInput(): Unit =
    notifyObservers(GameUpdate(currentState, Some("Please enter a valid letter.")))

  def quit(): Unit =
    notifyObservers(GameUpdate(currentState, Some("Game ended by player.")))

}
