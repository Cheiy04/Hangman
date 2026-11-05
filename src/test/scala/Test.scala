import hangman.Hangman
import hangman.controller.Controller
import hangman.model.{GameState, HangmanModel}
import hangman.observer.Observer
import hangman.view.ConsoleUI
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.nio.charset.StandardCharsets
import scala.collection.mutable.ListBuffer

class Test extends AnyWordSpec with Matchers {
  private class RecordingObserver extends Observer[GameState] {
    val updates: ListBuffer[GameState] = ListBuffer.empty

    override def update(state: GameState): Unit =
      updates += state
  }

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

  "GameState" when {
    "created" should {
      "represent a new game with the configured attempts" in {
        val state = GameState("hangman", Set.empty, 6, 6)

        state.word shouldBe "hangman"
        state.guessedWords shouldBe empty
        state.attemptsLeft shouldBe 6
        state.maxAttempts shouldBe 6
        state.isPlaying shouldBe true
        state.isFinished shouldBe false
      }

      "use six attempts by default" in {
        GameState("hangman", Set.empty, 6).maxAttempts shouldBe 6
      }
    }

    "receiving a guess" should {
      "add a correct letter without reducing attempts" in {
        val updated = GameState("hangman", Set.empty, 6, 6).guess('H')

        updated.guessedWords should contain('h')
        updated.attemptsLeft shouldBe 6
        updated.isWon shouldBe false
      }

      "add a wrong letter and reduce attempts" in {
        val updated = GameState("hangman", Set.empty, 6, 6).guess('z')

        updated.guessedWords should contain('z')
        updated.attemptsLeft shouldBe 5
        updated.isGameOver shouldBe false
      }

      "not change for a repeated letter" in {
        val state = GameState("hangman", Set.empty, 6, 6).guess('a')

        state.guess('A') shouldBe state
      }

      "not change after the game is finished" in {
        val state = GameState("hangman", Set.empty, 0, 0)

        state.guess('a') shouldBe state
      }
    }

    "all letters have been guessed" should {
      "be won and finished" in {
        val initial = GameState("cat", Set.empty, 6, 6)
        val won = initial.guess('c').guess('a').guess('t')

        won.isWon shouldBe true
        won.isFinished shouldBe true
        won.isPlaying shouldBe false
      }
    }

    "running out of attempts" should {
      "be game over without being a win" in {
        val gameOver = GameState("cat", Set.empty, 1, 1).guess('w')

        gameOver.attemptsLeft shouldBe 0
        gameOver.isGameOver shouldBe true
        gameOver.isFinished shouldBe true
        gameOver.isWon shouldBe false
      }
    }

    "displaying the word" should {
      "mask unguessed letters and reveal guessed letters" in {
        val state = GameState("hangman", Set('h', 'a'), 6, 6)

        state.maskedWord shouldBe "h a _ _ _ a _"
      }
    }
  }

  "HangmanModel" when {
    "starting a game" should {
      "store and notify observers of the initial state" in {
        val model = new HangmanModel
        val observer = new RecordingObserver

        model.addObserver(observer)
        val state = model.startGame("hangman", 4)

        model.currentState shouldBe state
        observer.updates should contain only state
      }
    }

    "receiving a guess" should {
      "update its state and notify observers" in {
        val model = new HangmanModel
        val observer = new RecordingObserver
        model.addObserver(observer)
        model.startGame("cat")

        val updated = model.guess('c')

        model.currentState shouldBe updated
        observer.updates should contain theSameElementsInOrderAs Seq(
          GameState("cat", Set.empty, 6, 6),
          updated
        )
      }

      "not notify observers when the state does not change" in {
        val model = new HangmanModel
        val observer = new RecordingObserver
        model.addObserver(observer)
        model.startGame("cat")
        model.guess('x')

        model.guess('X') shouldBe model.currentState
        observer.updates should have size 2
      }
    }

    "managing observers" should {
      "avoid duplicate registrations" in {
        val model = new HangmanModel
        val observer = new RecordingObserver
        model.addObserver(observer)
        model.addObserver(observer)

        model.startGame("cat")

        observer.updates should have size 1
      }

      "stop notifying a removed observer" in {
        val model = new HangmanModel
        val observer = new RecordingObserver
        model.addObserver(observer)
        model.removeObserver(observer)

        model.startGame("cat")

        observer.updates shouldBe empty
      }
    }

    "queried before a game starts" should {
      "report that no current state exists" in {
        val model = new HangmanModel

        an[IllegalStateException] should be thrownBy model.currentState
      }
    }
  }

  "Controller" when {
    "the player quits" should {
      "end the game with the quit message" in {
        val ui = new StubConsoleUI(List("quit"))

        new Controller(ui, "hangman").startGame()

        ui.welcomeShown shouldBe true
        ui.shownStates should have size 1
        ui.messages should contain("Game ended by player.")
      }
    }

    "the player enters invalid input" should {
      "keep the game running until a valid command is entered" in {
        val ui = new StubConsoleUI(List("1", "quit"))

        new Controller(ui, "hangman").startGame()

        ui.messages should contain("Please enter a valid letter.")
        ui.messages should contain("Game ended by player.")
        ui.shownStates should have size 1
      }
    }

    "the player wins" should {
      "announce the completed word" in {
        val ui = new StubConsoleUI(List("a"))

        new Controller(ui, "a").startGame()

        ui.shownStates.last.isWon shouldBe true
        ui.messages should contain("You won! The word was 'a'.")
      }
    }

    "the player runs out of attempts" should {
      "announce game over" in {
        val ui = new StubConsoleUI(List("z"))

        new Controller(ui, "a", maxAttempts = 1).startGame()

        ui.shownStates.last.isGameOver shouldBe true
        ui.messages should contain("Game over! The word was 'a'.")
      }
    }
  }

  "ConsoleUI" when {
    "showing the welcome message" should {
      "print the game instructions" in {
        val output = new ByteArrayOutputStream()

        Console.withOut(output) {
          new ConsoleUI().showWelcomeMessage()
        }

        val text = output.toString(StandardCharsets.UTF_8)
        text should include("Welcome to Hangman!")
        text should include("Guess one letter at a time. Type 'quit' to exit.")
      }
    }

    "receiving an observed state" should {
      "render the drawing and state values" in {
        val output = new ByteArrayOutputStream()
        val state = GameState("cat", Set('c', 'x'), 4, 6)

        Console.withOut(output) {
          new ConsoleUI().update(state)
        }

        val text = output.toString(StandardCharsets.UTF_8)
        text should include("|    O")
        text should include("|    |")
        text should include("Word: c _ _")
        text should include("Guessed letters: c, x")
        text should include("Attempts left: 4")
      }
    }

    "asking for a guess" should {
      "trim the input" in {
        val output = new ByteArrayOutputStream()
        val input = new ByteArrayInputStream("  a  \n".getBytes(StandardCharsets.UTF_8))

        val guess = Console.withIn(input) {
          Console.withOut(output) {
            new ConsoleUI().askForGuess()
          }
        }

        guess shouldBe "a"
        output.toString(StandardCharsets.UTF_8) should include("Enter a letter: ")
      }
    }

    "selecting a drawing" should {
      "show the final drawing after all attempts are used" in {
        val state = GameState("cat", Set.empty, 0, 6)

        new ConsoleUI().hangmanDrawing(state) should include("|   / \\")
      }
    }
  }

  "Hangman" when {
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
        text should include("Welcome to Hangman!")
        text should include("Game ended by player.")
      }
    }
  }
}
