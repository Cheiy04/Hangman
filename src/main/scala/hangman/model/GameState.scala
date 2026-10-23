package hangman.model

case class GameState(
   word: String,
   guessedWords: Set[Char],
   attemptsLeft: Int,
   maxAttempts: Int = 6
) {
  private val normalizedWord: String = word.toLowerCase

  def isWon: Boolean =
   normalizedWord.forall(ch => ch == ' ' || guessedWords.contains(ch))

  def isGameOver: Boolean = attemptsLeft <= 0

  def isFinished: Boolean = isWon || isGameOver

  def isPlaying: Boolean = !isFinished

  // hier we are replacing the dashed words with the correct guessed word
  def maskedWord: String =
   normalizedWord.map(ch => if (ch.isLetter && !guessedWords.contains(ch)) '_' else ch).mkString(" ")

  // instead of 2 guesses there is this one where the word is verified
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