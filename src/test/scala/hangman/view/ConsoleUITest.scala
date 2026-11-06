package hangman.view

import hangman.model.{GameState, GameUpdate}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.nio.charset.StandardCharsets

class ConsoleUITest extends AnyWordSpec with Matchers {
  "ConsoleUI" should {
    "show a welcome update" in {
      val output = new ByteArrayOutputStream
      Console.withOut(output) {
        new ConsoleUI().update(
          GameUpdate(
            GameState("hangman", Set.empty, 6, 6),
            Some("Welcome to Hangman!\nGuess one letter at a time. Type 'quit' to exit.")
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
      Console.withOut(output) { new ConsoleUI().update(GameUpdate(state)) }
      val text = output.toString(StandardCharsets.UTF_8)
      text should include("Word: c _ _")
      text should include("Guessed letters: c, x")
      text should include("Attempts left: 4")
    }

    "trim entered guesses" in {
      val input = new ByteArrayInputStream("  a  \n".getBytes(StandardCharsets.UTF_8))
      Console.withIn(input) { new ConsoleUI().askForGuess() } shouldBe "a"
    }

    "show the final hangman drawing" in {
      new ConsoleUI().hangmanDrawing(GameState("cat", Set.empty, 0, 6)) should include("|   / \\")
    }
  }
}
