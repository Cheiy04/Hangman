package hangman.model

case class GameUpdate(state: GameState, message: Option[String] = None)
