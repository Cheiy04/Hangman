package hangman.view

import hangman.model.{GameEvent, GameState, GameUpdate}
import hangman.common.GameInput
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.nio.charset.StandardCharsets

class ConsoleUITest extends AnyWordSpec with Matchers {
  private val ignoreInput: GameInput => Boolean = _ => false

  "ConsoleUI" should {
    "show a welcome update" in {
      val output = new ByteArrayOutputStream
      Console.withOut(output) {
        new ConsoleUI(ignoreInput).update(
          GameUpdate(
            GameState("hangman", Set.empty, 6, 6),
            Some(GameEvent.GameStarted)
          )
        )
      }
      val text = output.toString(StandardCharsets.UTF_8)
      text should include("Welcome to Hangman!")
      text should include("Guess one letter at a time. Type 'quit' to exit.")
    }

    "render an observed game state" in {
      val output = new ByteArrayOutputStream
      val state = GameState("cat", Set('c', 'x'), 4, 6)
      Console.withOut(output) { new ConsoleUI(ignoreInput).update(GameUpdate(state)) }
      val text = output.toString(StandardCharsets.UTF_8)
      text should include("Word: c _ _")
      text should include("Guessed letters: c, x")
      text should include("Attempts left: 4")
    }

    "render every domain event as console text" in {
      val state = GameState("cat", Set.empty, 4, 6)
      val expectedMessages = Map(
        GameEvent.GameStarted -> "Welcome to Hangman!",
        GameEvent.InvalidInput -> "Please enter a valid letter.",
        GameEvent.Won -> "You won! The word was 'cat'.",
        GameEvent.GameOver -> "Game over! The word was 'cat'.",
        GameEvent.Quit -> "Game ended by player."
      )

      expectedMessages.foreach { case (event, expectedMessage) =>
        val output = new ByteArrayOutputStream
        Console.withOut(output) {
          new ConsoleUI(ignoreInput).update(GameUpdate(state, Some(event)))
        }
        output.toString(StandardCharsets.UTF_8) should include(expectedMessage)
      }
    }

    "send normalized input to the callback and stop when it returns false" in {
      val input = new ByteArrayInputStream("  a  \n".getBytes(StandardCharsets.UTF_8))
      var received: Option[GameInput] = None
      val ui = new ConsoleUI(gameInput => {
        received = Some(gameInput)
        false
      })

      Console.withIn(input) { ui.startInput() }
      received shouldBe Some(GameInput.Guess('a'))
    }

    "send invalid input to the callback" in {
      val input = new ByteArrayInputStream("ab\n".getBytes(StandardCharsets.UTF_8))
      var received: Option[GameInput] = None
      val ui = new ConsoleUI(gameInput => {
        received = Some(gameInput)
        false
      })

      Console.withIn(input) { ui.startInput() }
      received shouldBe Some(GameInput.Invalid)
    }

    "continue reading while the callback accepts input" in {
      val input = new ByteArrayInputStream("a\nquit\n".getBytes(StandardCharsets.UTF_8))
      var received = List.empty[GameInput]
      val ui = new ConsoleUI(gameInput => {
        received = received :+ gameInput
        received.size < 2
      })

      Console.withIn(input) { ui.startInput() }
      received shouldBe List(GameInput.Guess('a'), GameInput.Quit)
    }

    "show the final hangman drawing" in {
      new ConsoleUI(ignoreInput).hangmanDrawing(GameState("cat", Set.empty, 0, 6)) should include("|   / \\")
    }
  }
}
