package hangman.controller

import hangman.common.GameInput
import hangman.model.{GameModel, GameState, HangmanModel}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ControllerTest extends AnyWordSpec with Matchers {
  private class StubModel extends GameModel {
    private var state: Option[GameState] = None

    override def currentState: GameState =
      state.getOrElse(throw new IllegalStateException("The game has not been started."))

    override def startGame(word: String, maxGuesses: Int): GameState = {
      val newState = GameState(word, Set.empty, maxGuesses, maxGuesses)
      state = Some(newState)
      newState
    }

    override def guess(letter: Char): GameState = {
      val newState = currentState.guess(letter)
      state = Some(newState)
      newState
    }

    override def invalidInput(): Unit = ()

    override def quit(): Unit = ()
  }

  "Controller" when {
    "started" should {
      "work with the model abstraction" in {
        val model = new StubModel
        val controller = new Controller(model, "a")

        controller.startGame()
        controller.handleInput(GameInput.Guess('a')) shouldBe false
        model.currentState.isWon shouldBe true
      }

      "start the injected model without reading input" in {
        val model = new HangmanModel
        new Controller(model, "hangman").startGame()

        model.currentState.word shouldBe "hangman"
      }

      "handle invalid input and a win" in {
        val model = new HangmanModel
        val controller = new Controller(model, "a")
        controller.startGame()

        controller.handleInput(GameInput.Invalid) shouldBe true
        controller.handleInput(GameInput.Guess('a')) shouldBe false
        model.currentState.isWon shouldBe true
      }

      "announce game over" in {
        val model = new HangmanModel
        val controller = new Controller(model, "a", 1)
        controller.startGame()

        controller.handleInput(GameInput.Guess('z')) shouldBe false
        model.currentState.isGameOver shouldBe true
      }

      "stop input processing when the player quits" in {
        val model = new HangmanModel
        val controller = new Controller(model, "a")
        controller.startGame()

        controller.handleInput(GameInput.Quit) shouldBe false
      }
    }
  }
}
