package hangman.model

import hangman.observer.Observer
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class HangmanModelTest extends AnyWordSpec with Matchers {
  private class RecordingObserver extends Observer[GameUpdate] {
    var updates: List[GameUpdate] = Nil
    override def update(update: GameUpdate): Unit = updates = updates :+ update
  }

  "HangmanModel" when {
    "starting a game" should {
      "store and notify the initial state" in {
        val model = new HangmanModel
        val observer = new RecordingObserver
        model.addObserver(observer)

        val state = model.startGame("hangman", 4)

        model.currentState shouldBe state
        observer.updates.head.state shouldBe state
        observer.updates.head.event shouldBe Some(GameEvent.GameStarted)
      }

      "publish a quit event without presentation text" in {
        val model = new HangmanModel
        val observer = new RecordingObserver
        model.addObserver(observer)
        model.startGame("hangman")

        model.quit()

        observer.updates.last.event shouldBe Some(GameEvent.Quit)
      }
    }

    "receiving a guess" should {
      "update and notify observers" in {
        val model = new HangmanModel
        val observer = new RecordingObserver
        model.addObserver(observer)
        model.startGame("cat")

        val state = model.guess('c')

        model.currentState shouldBe state
        observer.updates.last.state shouldBe state
        observer.updates.last.event shouldBe None
      }

      "publish domain events instead of presentation messages" in {
        val model = new HangmanModel
        val observer = new RecordingObserver
        model.addObserver(observer)
        model.startGame("a")

        model.invalidInput()
        observer.updates.last.event shouldBe Some(GameEvent.InvalidInput)

        model.guess('a')
        observer.updates.last.event shouldBe Some(GameEvent.Won)
      }

      "publish a game-over event for the final wrong guess" in {
        val model = new HangmanModel
        val observer = new RecordingObserver
        model.addObserver(observer)
        model.startGame("a", 1)

        model.guess('z')

        observer.updates.last.event shouldBe Some(GameEvent.GameOver)
      }

      "not notify for an unchanged state" in {
        val model = new HangmanModel
        val observer = new RecordingObserver
        model.addObserver(observer)
        model.startGame("cat")
        model.guess('x')
        model.guess('X')
        observer.updates should have size 2
      }
    }

    "managing observers" should {
      "avoid duplicate registrations and support removal" in {
        val model = new HangmanModel
        val observer = new RecordingObserver
        model.addObserver(observer)
        model.addObserver(observer)
        model.startGame("cat")
        observer.updates should have size 1

        model.removeObserver(observer)
        model.startGame("dog")
        observer.updates should have size 1
      }
    }

    "queried before starting" should {
      "throw an explicit error" in {
        val error = the[IllegalStateException] thrownBy new HangmanModel().currentState
        error.getMessage shouldBe "The game has not been started."
      }
    }
  }
}
