package hangman.model

enum GameEvent {
  case GameStarted
  case InvalidInput
  case Won
  case GameOver
  case Quit
}
