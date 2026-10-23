package hangman.view

import scala.io.StdIn
// add import of game state from hier to do
// Display content on the console
class ConsoleUI {
  //initial messages
  def showWelcomeMessage(): Unit =
    println("Welcome to Hangman!")
    println("Guess one letter at a time. Type 'quit' to exit.")

  //showing the current state iof the game
  def showState(state: hangman.model.GameState): Unit =
    println(s"Word: ${state.maskedWord}")
    println(s"Guessed letters: ${state.guessedWords.mkString(", ")}")
    println(s"Attempts left: ${state.attemptsLeft}")

  //taking the input from user
  def askForGuess(): String =
    print("Enter a letter: ")
    StdIn.readLine().trim

  def showMessage(message: String): Unit =
    println(message)
}
