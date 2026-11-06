package hangman.view

import scala.io.StdIn
import hangman.model.{GameState, GameUpdate}
import hangman.observer.Observer
// Display content on the console
class ConsoleUI extends Observer[GameUpdate] {
  private val hangmanStages: Vector[String] = Vector(
    """
#      |-------
#      |
#      |
#      |
#      |-------
      """.stripMargin('#'),
    """
#      |-------
#      |    O
#      |
#      |
#      |-------
      """.stripMargin('#'),
    """
#      |-------
#      |    O
#      |    |
#      |
#      |-------
      """.stripMargin('#'),
    """
#      |-------
#      |    O
#      |   /|
#      |
#      |-------
      """.stripMargin('#'),
    """
#      |-------
#      |    O
#      |   /|\
#      |
#      |-------
      """.stripMargin('#'),
    """
#      |-------
#      |    O
#      |   /|\
#      |   /
#      |-------
      """.stripMargin('#'),
    """
#      |-------
#      |    O
#      |   /|\
#      |   / \
#      |-------
      """.stripMargin('#')
  )

  def hangmanDrawing(state: GameState): String = {
    val wrongGuesses = state.maxAttempts - state.attemptsLeft
    val index = wrongGuesses.max(0).min(hangmanStages.length - 1)
    hangmanStages(index)
  }

  //showing the current state iof the game
  def showState(state: GameState): Unit =
    println(hangmanDrawing(state))
    println(s"Word: ${state.maskedWord}")
    println(s"Guessed letters: ${state.guessedWords.mkString(", ")}")
    println(s"Attempts left: ${state.attemptsLeft}")

  // the observer function is implemented here
  override def update(update: GameUpdate): Unit = {
    showState(update.state)
    update.message.foreach(println)
  }

  //taking the input from user
  def askForGuess(): String =
    print("Enter a letter: ")
    StdIn.readLine().trim

}
