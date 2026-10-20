package hangman
// this will be the entry point of the entire program and bind the components

import model.State
object Hangman {
    val totalAttempts = 6
    
    def run(): Unit = {
        
        print("Welcome to hangman.")
        State.newGame()
        // Display formatted word
        // Accept guess
        // Check if guess is correct
        // Update word on screen
        // Accept guess - loop
    }
}