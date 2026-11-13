package hangman.model

case class GameUpdate(state: GameState, event: Option[GameEvent] = None)
