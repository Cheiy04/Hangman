package hangman.model

import java.util.Scanner

//model for showing the current game of the game after a given method call
case class State(hp: Int, word : String) {
  /** Starts a new play with a designated word */
  def newGame(): Unit = {
    val totalAttempts : Int = 6
    var playing : Boolean = true
    var gameWon: Boolean = false
    val word : String = "Test" // Will be API call
    val wordState: String = "XXXX" // will have a method to format this
    /** 
     * 
     * */
    while (playing) {
      
    }
    
  }
  
  /** Game entry: request player name & ask for word level(easy, medium, hard)
   * */
  
  
  /** Prints out the word (formatted to show only correct letters)
   * and accepts new input. Show available guesses if prompted.
   * @param hp old HP, will be updated at the end after checking
   * @param word current formatted string(z.B. bXXk)
   * @return ??? bool            
   * */
  def newTry(hp : Int, word: String): Boolean = {
    // assume it is checked that enough HP is remaining
    print(word)
    print("Enter the next guess. Press 1 to show available letters")
    val in = new Scanner(System.in)
    val ans : String = in.next()
    
    if (ans.equals("1")) {
      // method call for showing available letters in an array
    } else if (ans.isBlank || ans.isEmpty) {
      printf("Enter a valid letter")
    }
    // return validationMethod()
    false
  }
  
  /**
   * Reveals the answer when hp is 0 and gives the user choice to end game or play again.
  * */
  def gameOver(): Unit = {
    printf(f"Game over. The word was $word")
    printf("Enter new game?: (y/n)\n")
    val ans = scala.io.StdIn.readLine()
    if ans == "y"
      // newGame()
    else
      System.exit(0)
  }
}
