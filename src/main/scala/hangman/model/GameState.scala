package hangman.model
import java.lang.StringBuilder
//model for showing the current game of the game after a given method call
case class GameState(word : String, guessedWords : Set[Char], maxGuesses : Int) :
  
  //conditions that the game is finished
  def isFinished : Boolean =
    if maxGuesses < 1 || isWon || gameOver then
      true
    else
      false

  //we will implement the win conditions of the game - to be implemented
  def isWon : Boolean =
    true
  
  // Bedingung for a lost state- to be implemented
  def gameOver : Boolean =
    true
  
  // Bedingung for our playing state - to be implemented
  def isPlaying : Boolean =
    if !isFinished then 
      true
    else
      false
      
  