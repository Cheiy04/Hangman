/*
Hangman scala.
Purpose:
*   Implementation worksheet for our scala-based Hangman game
*
*Oder of work implementation:
*   1. Imports and basic usable variable
*   2. Creation and implementation of our methods
*   3. Get a running version on the console
*   4. Implementation of a TUI
*   5. Get multiple words form an API
*
*
*
*

*
*/

// 1. Imports and basic usable variables
//--------------------------------------------
    //1.1 -->

import scala.io.StdIn
import scala.util.Random

val Word: String = "Hangman"
val letter: Character = 'g'
val totalAttempts: Int = 6
val gameWon: Boolean = false
var remainingAttempts: Int = totalAttempts
remainingAttempts-= 1

println(s"You have attempts $remainingAttempts left!")
val Status =
  if(remainingAttempts > 0) "Playing"
  else "Game over!"

println(s"Game status: $Status")

    //1.2 --> Character manipulation