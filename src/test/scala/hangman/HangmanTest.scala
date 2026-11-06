package hangman

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.nio.charset.StandardCharsets

class HangmanTest extends AnyWordSpec with Matchers {
  "Hangman" when {
    "started" should {
      "run a playable game loop" in {
        val output = new ByteArrayOutputStream
        val input = new ByteArrayInputStream("quit\n".getBytes(StandardCharsets.UTF_8))
        Console.withIn(input) {
          Console.withOut(output) { Hangman.main(Array("scala")) }
        }
        val text = output.toString(StandardCharsets.UTF_8)
        text should include("Welcome to Hangman!")
        text should include("Game ended by player.")
      }
    }
  }
}
