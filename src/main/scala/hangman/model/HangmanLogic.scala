package hangman.model
// defines the rules of the game and how the game logic will work
object HangmanLogic :
  def startGame(word : String, maxGuesses:Int = 6): GameState =
    //the start game function returns a game state where the parameters will be(word, guessedWord, maxGuesses)
    GameState(
      word = word,
      guessedWords = Set.empty,
      maxGuesses = maxGuesses
    )

  //we will call this function by every input, it checks the current state of the game and if not game over then it will
  //add a new letter into the Set
  def guess(state : GameState, letter : Char) : GameState =
    if state.isFinished then
      state
    else { 
      verifiedGuess(state, state.word, letter)
      state
    }
  
  // verify if the letter given is in the string if yes then a new state is created and if no then the guess count -1
  def verifiedGuess(state : GameState, word : String, letter : Char) : Unit =
    if state.guessedWords.forall(x => word.contains(x) ) then 
      state.copy(guessedWords = state.guessedWords + letter)// the copy() return another instance of the case class but with changed params
    else
      state.copy(maxGuesses = state.maxGuesses - 1 )
        