import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import hangman.Hangman
import hangman.controller.Controller
import hangman.model.GameState
import hangman.model.HangmanLogic
import hangman.view.ConsoleUI

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import scala.collection.mutable.ListBuffer

class Test extends AnyWordSpec with Matchers {
  private class StubConsoleUI(initialInputs: List[String]) extends ConsoleUI {
    private var remainingInputs: List[String] = initialInputs

    var welcomeShown: Boolean = false
    val shownStates: ListBuffer[GameState] = ListBuffer.empty
    val messages: ListBuffer[String] = ListBuffer.empty

    override def showWelcomeMessage(): Unit =
      welcomeShown = true

    override def showState(state: GameState): Unit =
      shownStates += state

    override def askForGuess(): String = {
      val next = remainingInputs.headOption.getOrElse("quit")
      remainingInputs = remainingInputs.drop(1)
      next
    }

    override def showMessage(message: String): Unit =
      messages += message
  }

  //Testing the Game states and what happens at different states of the game according to the logic written
  "A Hangman game" when {
    "starting a new game" should {
      "initialize a clean state" in {
        val state = HangmanLogic.startGame("hangman", 6)

        state.word shouldBe "hangman"
        state.guessedWords shouldBe Set.empty
        state.attemptsLeft shouldBe 6
        state.isPlaying shouldBe true
        state.isFinished shouldBe false
      }
    }
    "initialising a game without giving the number of max guesses" should{
      "use the already given max guesses without having to give it ourselves" in{
        val state = HangmanLogic.startGame("hangman")
        state.maxAttempts shouldBe 6
      }
    }
    

    "guessing a correct letter" should {
      "update the guessed set without losing a turn" in {
        val state = HangmanLogic.startGame("hangman", 6)
        val updated = HangmanLogic.guess(state, 'h')
        updated.guessedWords should contain('h')
        updated.attemptsLeft shouldBe 6
        updated.isWon shouldBe false
      }
    }

    "guessing a wrong letter" should {
      "reduce attempts" in {
        val state = HangmanLogic.startGame("hangman", 6)
        val updated = HangmanLogic.guess(state, 'z')

        updated.guessedWords should contain('z')
        updated.attemptsLeft shouldBe 5
        updated.isGameOver shouldBe false
      }
    }

    "all letters have been guessed" should{
      "show state game won" in{
        val state = HangmanLogic.startGame("cat", 6)
        val addC = HangmanLogic.guess(state, 'c')
        val addA = HangmanLogic.guess(addC, 'a')
        val addT = HangmanLogic.guess(addA, 't')

        addT.isWon shouldBe true
        addT.isFinished shouldBe true
        addT.isPlaying shouldBe false
      }
    }

    "no more attempts" should {
      "show that game is over" in{
        val state = HangmanLogic.startGame("cat", 1)
        val lastGuess = HangmanLogic.guess(state, 'w')

        lastGuess.attemptsLeft shouldBe 0
        lastGuess.isFinished shouldBe true
        lastGuess.isGameOver shouldBe true
        lastGuess.isWon shouldBe false
      }
    }

    "Words that have not yet been guessed correctly" should{
      "hide the final word with dashes" in{
        val state = HangmanLogic.startGame("hangman", 6)
        val update = HangmanLogic.guess(state,'h')

        update.maskedWord shouldBe "h _ _ _ _ _ _"
      }
    }

    "Trying to make new guesses for a finished state" should{
      "return the current game state without making any changes" in{
        val state = HangmanLogic.startGame("hangman",0)
        if state.isFinished then
          state.guess('a') shouldBe state
      }
    }

    "Trying to guess a letter that has already been guessed before" should{
      "return the same state without re-adding the letter into the list" in{
        val state = HangmanLogic.startGame("hangman",6)
        val guessedWord = state.guess('a')
        if !guessedWord.isFinished then
          guessedWord.guess('a') shouldBe guessedWord
        //val repeatedGuess = guessedWord.guess('a')

      }
    }

  }

  "The Controller" when {
    "the player quits" should {
      "end the game with the quit message" in {
        val ui = new StubConsoleUI(List("quit"))
        val controller = new Controller(ui, "hangman")

        controller.startGame()

        ui.welcomeShown shouldBe true
        ui.messages should contain ("Game ended by player.")
      }
    }

    "the player enters invalid input" should {
      "ask for a valid letter and keep playing" in {
        val ui = new StubConsoleUI(List("1", "quit"))
        val controller = new Controller(ui, "hangman")

        controller.startGame()

        ui.messages should contain ("Please enter a valid letter.")
        ui.messages should contain ("Game ended by player.")
      }
    }

    "the player guesses the full word" should {
      "announce the win" in {
        val ui = new StubConsoleUI(List("a"))
        val controller = new Controller(ui, "a")

        controller.startGame()

        ui.messages should contain ("You won! The word was 'a'.")
      }
    }

    "the player runs out of attempts" should {
      "announce game over" in {
        val ui = new StubConsoleUI(List("z"))
        val controller = new Controller(ui, "a", maxAttempts = 1)

        controller.startGame()

        ui.messages should contain ("Game over! The word was 'a'.")
      }
    }

    "the player loses after a failed guess" should {
      "reach the game-over branch in the controller" in {
        val ui = new StubConsoleUI(List("z"))
        val controller = new Controller(ui, "a", maxAttempts = 1)

        controller.startGame()

        ui.messages should contain ("Game over! The word was 'a'.")
      }
    }
  }

  "The ConsoleUI" when {
    "showing the welcome message" should {
      "print the game instructions" in {
        val ui = new ConsoleUI
        val output = new ByteArrayOutputStream()

        Console.withOut(output) {
          ui.showWelcomeMessage()
        }

        val text = output.toString(StandardCharsets.UTF_8)
        text should include ("Welcome to Hangman!")
        text should include ("Guess one letter at a time. Type 'quit' to exit.")
      }
    }

    "showing the game state" should {
      "include hangman drawing and state values" in {
        val ui = new ConsoleUI
        val state = GameState(word = "cat", guessedWords = Set('c', 'x'), attemptsLeft = 4, maxAttempts = 6)
        val output = new ByteArrayOutputStream()

        Console.withOut(output) {
          ui.showState(state)
        }

        val text = output.toString(StandardCharsets.UTF_8)
        text should include ("|    O")
        text should include ("|    |")
        text should include ("Word: c _ _")
        text should include ("Guessed letters: c, x")
        text should include ("Attempts left: 4")
      }
    }

    "asking for a guess" should {
      "trim the input value" in {
        val ui = new ConsoleUI
        val output = new ByteArrayOutputStream()
        val input = new ByteArrayInputStream("  a  \n".getBytes(StandardCharsets.UTF_8))

        val guess = Console.withIn(input) {
          Console.withOut(output) {
            ui.askForGuess()
          }
        }

        guess shouldBe "a"
        output.toString(StandardCharsets.UTF_8) should include ("Enter a letter: ")
      }
    }

    "selecting a hangman drawing" should {
      "show the final drawing after all attempts are used" in {
        val ui = new ConsoleUI
        val state = GameState(word = "cat", guessedWords = Set.empty, attemptsLeft = 0, maxAttempts = 6)

        val drawing = ui.hangmanDrawing(state)

        drawing should include ("|   / \\")
      }
    }
  }

  "The Hangman entrypoint" when {
    "main is called" should {
      "start a playable game loop" in {
        val output = new ByteArrayOutputStream()
        val input = new ByteArrayInputStream("quit\n".getBytes(StandardCharsets.UTF_8))

        Console.withIn(input) {
          Console.withOut(output) {
            Hangman.main(Array("scala"))
          }
        }

        val text = output.toString(StandardCharsets.UTF_8)
        text should include ("Welcome to Hangman!")
        text should include ("Game ended by player.")
      }
    }
  }
}
