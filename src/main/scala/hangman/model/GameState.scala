package hangman.model

//model for showing the current game of the game after a given method call
case class GameState(word: String, guessedWords: Set[Char], attemptsLeft: Int) {
  var playing : Boolean = true

  //conditions that the game is finished
  def isFinished: Boolean =
    if attemptsLeft < 1 || isWon || isGameOver then
      true
    else
      false

  //we will implement the win conditions of the game - to be implemented
  def isWon: Boolean =
    if (attemptsLeft > 0 && guessedWords.toString() == word) true else false

  // Bedingung for a lost state- to be implemented
  def isGameOver: Boolean =
    if (attemptsLeft < 1 && guessedWords.toString() != word) true else false

  // Bedingung for our playing state - to be implemented
  def isPlaying: Boolean =
    if !isFinished then
      true
    else
      false

  /** update current hp or pop a balloon in GUI
   *
   * @return new hp
   * */
  def popABalloon(state : GameState): GameState = state.copy(attemptsLeft = state.attemptsLeft - 1)

  /** updates the player's score
   * TODO: When player classes are implemented
   * @param Player player class that has the score for individual players stored
   * @return Player player class with an updated score
   * */
  // def updateScore() : Unit

  /** victory: called when the condition isWon is true, returns a state (on console prnt)*/
  def victory() : Unit = {
    print("You guessed it right!")
  }

  def gameOver(state: GameState) : Unit = {
    print(f"The word was ${state.word}. Better luck next time!")
  }
}