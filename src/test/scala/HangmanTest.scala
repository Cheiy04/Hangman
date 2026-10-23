import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import hangman.model.{GameState, HangmanLogic}

class HangmanTest extends AnyFunSuite with Matchers {

  test("startGame initializes a clean state") {
    val state = HangmanLogic.startGame("hangman", 6)

    state.word shouldBe "hangman"
    state.guessedWords shouldBe Set.empty
    state.attemptsLeft shouldBe 6
    state.isPlaying shouldBe true
    state.isFinished shouldBe false
  }

  test("guessing a correct letter updates the guessed set without losing a turn") {
    val state = HangmanLogic.startGame("hangman", 6)
    val updated = HangmanLogic.guess(state, 'h')

    updated.guessedWords should contain ('h')
    updated.attemptsLeft shouldBe 6
    updated.isWon shouldBe false
  }

  test("guessing a wrong letter reduces attempts") {
    val state = HangmanLogic.startGame("hangman", 6)
    val updated = HangmanLogic.guess(state, 'z')

    updated.guessedWords should contain ('z')
    updated.attemptsLeft shouldBe 5
    updated.isGameOver shouldBe false
  }

 
  test(""){
    
  }
}
