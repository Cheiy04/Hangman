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

  // i changed the implementation of guess and put it in state and just called it hier to check if the state is correct
  def guess(state: GameState, letter: Char): GameState =
    if (state.isFinished) state else state.guess(letter)

  //same thing wiht verify i put the implementaion on state
  def verifyGuess(state: GameState, letter: Char): GameState =
    guess(state, letter)

  def isWon(state: GameState): Boolean = state.isWon

  def isGameOver(state: GameState): Boolean = state.isGameOver
}