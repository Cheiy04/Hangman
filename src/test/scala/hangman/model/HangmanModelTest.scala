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
        observer.updates.head.message shouldBe defined
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
        an[IllegalStateException] should be thrownBy new HangmanModel().currentState
      }
    }
  }
}
