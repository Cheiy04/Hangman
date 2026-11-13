package hangman.model

import hangman.observer.Observable

trait GameModel extends Observable[GameUpdate] {
  def currentState: GameState
  def startGame(word: String, maxGuesses: Int): GameState
  def guess(letter: Char): GameState
  def invalidInput(): Unit
  def quit(): Unit
}
