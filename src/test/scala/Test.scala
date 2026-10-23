import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import hangman.model.HangmanLogic

class Test extends AnyWordSpec with Matchers {

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

  }
}
