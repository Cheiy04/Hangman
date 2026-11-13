package hangman.common

enum GameInput {
  case Guess(letter: Char)
  case Quit
  case Invalid
}