package hangman.model

class HangmanModel extends GameModel {
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
    notifyObservers(GameUpdate(newState, Some(GameEvent.GameStarted)))
    newState
  }

  def guess(letter: Char): GameState = {
    val previousState = currentState
    val newState = previousState.guess(letter)
    state = Some(newState)
    if newState != previousState then
      val event =
        if newState.isWon then Some(GameEvent.Won)
        else if newState.isGameOver then Some(GameEvent.GameOver)
        else None
      notifyObservers(GameUpdate(newState, event))
    newState
  }

  def invalidInput(): Unit =
    notifyObservers(GameUpdate(currentState, Some(GameEvent.InvalidInput)))

  def quit(): Unit =
    notifyObservers(GameUpdate(currentState, Some(GameEvent.Quit)))

}
