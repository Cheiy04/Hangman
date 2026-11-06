package hangman.controller

import hangman.model.HangmanModel
import hangman.model.{GameState, GameUpdate}
import hangman.view.ConsoleUI
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ControllerTest extends AnyWordSpec with Matchers {
  private class StubConsoleUI(initialInputs: List[String]) extends ConsoleUI {
    private var inputs = initialInputs
    var shownStates = List.empty[GameState]
    var messages = List.empty[String]

    override def update(update: GameUpdate): Unit = {
      shownStates = shownStates :+ update.state
      messages = messages ++ update.message
    }
    override def askForGuess(): String = {
      val input = inputs.headOption.getOrElse("quit")
      inputs = inputs.drop(1)
      input
    }
  }

  "Controller" when {
    "started" should {
      "use the injected model and observer view" in {
        val ui = new StubConsoleUI(List("quit"))
        val model = new HangmanModel
        new Controller(ui, model, "hangman").startGame()

        ui.shownStates should have size 2
        model.currentState.word shouldBe "hangman"
        ui.messages should contain("Game ended by player.")
      }

      "handle invalid input and a win" in {
        val ui = new StubConsoleUI(List("1", "a"))
        new Controller(ui, new HangmanModel, "a").startGame()

        ui.messages should contain("Please enter a valid letter.")
        ui.messages should contain("You won! The word was 'a'.")
        ui.shownStates.last.isWon shouldBe true
      }

      "announce game over" in {
        val ui = new StubConsoleUI(List("z"))
        new Controller(ui, new HangmanModel, "a", 1).startGame()

        ui.messages should contain("Game over! The word was 'a'.")
        ui.shownStates.last.isGameOver shouldBe true
      }
    }
  }
}
