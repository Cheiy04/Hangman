package hangman.model

object HangmanLogic {
  // logic to init game by getting an instance of the Gamestate
  def startGame(word: String, maxGuesses: Int = 6): GameState =
    GameState(
      word = word,
      guessedWords = Set.empty,
      attemptsLeft = maxGuesses,
      maxAttempts = maxGuesses
    )
  def guess(state : GameState, letter : Char) : GameState =
    state.guess(letter)

}