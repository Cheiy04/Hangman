package hangman.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GameStateTest extends AnyWordSpec with Matchers {
  "GameState" when {
    "created" should {
      "represent a new game" in {
        val state = GameState("hangman", Set.empty, 6, 6)
        state.isPlaying shouldBe true
        state.isFinished shouldBe false
        state.attemptsLeft shouldBe 6
      }

      "use six attempts by default" in {
        GameState("hangman", Set.empty, 6).maxAttempts shouldBe 6
      }
    }

    "receiving guesses" should {
      "accept a correct letter without reducing attempts" in {
        val state = GameState("hangman", Set.empty, 6, 6).guess('H')
        state.guessedWords should contain('h')
        state.attemptsLeft shouldBe 6
      }

      "reduce attempts for a wrong letter" in {
        GameState("hangman", Set.empty, 6, 6).guess('z').attemptsLeft shouldBe 5
      }

      "ignore repeated and post-game guesses" in {
        val state = GameState("hangman", Set.empty, 6, 6).guess('a')
        state.guess('A') shouldBe state
        GameState("hangman", Set.empty, 0, 0).guess('a') shouldBe
          GameState("hangman", Set.empty, 0, 0)
      }
    }

    "completed" should {
      "be won when every letter is guessed" in {
        val state = GameState("cat", Set.empty, 6, 6).guess('c').guess('a').guess('t')
        state.isWon shouldBe true
        state.isFinished shouldBe true
      }

      "be game over when attempts reach zero" in {
        val state = GameState("cat", Set.empty, 1, 1).guess('w')
        state.isGameOver shouldBe true
        state.isWon shouldBe false
      }
    }

    "displaying the word" should {
      "mask unguessed letters" in {
        GameState("hangman", Set('h', 'a'), 6, 6).maskedWord shouldBe "h a _ _ _ a _"
      }
    }
  }
}
