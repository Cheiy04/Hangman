package hangman.view

import hangman.common.GameInput

import hangman.model.{GameEvent, GameState, GameUpdate}
import hangman.observer.Observer
import scala.io.StdIn

class ConsoleUI(onInput: GameInput => Boolean) extends Observer[GameUpdate] {
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

  def startInput(): Unit =
    while (onInput(readInput())) {}

  private def readInput(): GameInput = {
    print("Enter a letter: ")
    val input = Option(StdIn.readLine()).fold("quit")(_.trim.toLowerCase)

    if (input == "quit") {
      GameInput.Quit
    } else if (input.length == 1 && input.head.isLetter) {
      GameInput.Guess(input.head)
    } else {
      GameInput.Invalid
    }
  }

  def hangmanDrawing(state: GameState): String = {
    val wrongGuesses = state.maxAttempts - state.attemptsLeft
    val index = wrongGuesses.max(0).min(hangmanStages.length - 1)
    hangmanStages(index)
  }

  private def showState(state: GameState): Unit = {
    println(hangmanDrawing(state))
    println(s"Word: ${state.maskedWord}")
    println(s"Guessed letters: ${state.guessedWords.mkString(", ")}")
    println(s"Attempts left: ${state.attemptsLeft}")
  }

  override def update(update: GameUpdate): Unit = {
    showState(update.state)
    update.event.foreach(event => println(messageFor(event, update.state)))
  }

  private def messageFor(event: GameEvent, state: GameState): String =
    event match {
      case GameEvent.GameStarted =>
        "Welcome to Hangman!\nGuess one letter at a time. Type 'quit' to exit."
      case GameEvent.InvalidInput =>
        "Please enter a valid letter."
      case GameEvent.Won =>
        s"You won! The word was '${state.word}'."
      case GameEvent.GameOver =>
        s"Game over! The word was '${state.word}'."
      case GameEvent.Quit =>
        "Game ended by player."
    }
}
