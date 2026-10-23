package hangman.model

case class GameState(
   word: String,
   guessedWords: Set[Char],
   attemptsLeft: Int,
   maxAttempts: Int = 6
) {
  private val normalizedWord: String = word.toLowerCase //converts the word into a standard form

  def isWon: Boolean =
   normalizedWord.forall(ch => guessedWords.contains(ch))

  def isGameOver: Boolean = attemptsLeft <= 0

  def isFinished: Boolean = isWon || isGameOver

  def isPlaying: Boolean = !isFinished

  // hier we are checking each letter of our word and if not guessed then hide it with _ else show char
  // the mkString take our collection and combines it into a string with space
  def maskedWord: String =
   normalizedWord.map(ch => if (ch.isLetter && !guessedWords.contains(ch)) '_' else ch).mkString(" ")

  // instead of 2 guesses there is this one where the word is verified
  // the guess takes in a single char performs the functions and then return a new game state
  def guess(letter: Char): GameState = {
   if isFinished then this
   else
     val guessedLetter = letter.toLower
     if (guessedWords.contains(guessedLetter)) this
     else
       val nextGuesses = guessedWords + guessedLetter
       if (normalizedWord.contains(guessedLetter))
         copy(guessedWords = nextGuesses)
       else
         copy(guessedWords = nextGuesses, attemptsLeft = attemptsLeft - 1)
  }

  def victory(): Unit = println(s"You guessed it right! The word was: $word")

  def gameOver(): Unit = println(s"The word was: $word. Better luck next time!")
}