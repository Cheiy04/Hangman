/*
Hangman scala.
Purpose:
*   Implementation worksheet for our scala-based Hangman game
*
*Oder of work implementation:
*   1. Imports and basic usable variable
        - Assertions trait gives us a TestFailException instead of an assertio error(more useful)-import org.scalatest.Assertions._
*   2. Creation and implementation of our methods
*   3. Get a running version on the console
*   4. Implementation of a TUI
*   5. Get multiple words form an API
*
*hint
*give game instruction at the start
hint
*game difficulty - number of ballons does not reset in hard mode
*adding rope man over crocodile water
*
*create tests
*newgame(), displayblanks(), verifyletter(), fillblank(), popBallon(), gameover(), victory(), updatescore(), updateHP()
*quitgame(), timespentplaying(), highscore()
*
*Game logic and GameState
  Logic- implemented as an object which is returns new states, startgame(), guess(), verifyletter(), endgame(), updatescore()
  State- implemented as a case class with game parameters(word, letterguessed, maxguesses)
*/

// 1. Imports and basic usable variables
//--------------------------------------------
    //1.1 -->

import hangman.model.GameState

import scala.io.StdIn
import scala.util.Random


val Word: String = "Hangman"
val letter: Character = 'g'
val totalAttempts: Int = 6
var currentScore: Int = 4
val gameWon: Boolean = false
val correctWord: Boolean = false
var remainingAttempts: Int = totalAttempts
remainingAttempts-= 1

println(s"You have attempts $remainingAttempts left!")
val Status =
  if(remainingAttempts > 0) "Playing"
  else "Game over!"

println(s"Game status: $Status")

    //1.2 --> Character manipulation

val x = 12
val y = 11
assert(x!=y)


// defines the rules of the game and how the game logic will work
object HangmanLogic :
  def startGame(word : String, maxGuesses:Int = 6): GameState =
    //the start game function returns a game state where the parameters will be(word, guessedWord, maxGuesses)
    GameState(
      word,
      Set.empty,
      attemptsLeft = maxGuesses
    )
    

