package com.example.guessthatcat.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.guessthatcat.R
import com.example.guessthatcat.data.allCats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/*viewmodel contains functionality of the app picking words, shuffling, resetting
* is immutable to other classes (read-only)
* */
class GameViewModel : ViewModel() {

    // Game UI state
    private val _uiState = MutableStateFlow(GameUiState())

    // Set of words used in the game
    private var usedWords: MutableSet<String> = mutableSetOf()
    var userGuess by mutableStateOf("")
        private set

    // Backing property to avoid state updates from other classes
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        resetGame()
    }

    fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(currentCat = pickRandomCat())
        updateCurrentCatImage()
    }

    private fun pickRandomCat(): String {
        currentCat = allCats.random()
        return currentCat
    }
    fun updateUserGuess(guessedWord: String) {
        userGuess = guessedWord
    }

    private lateinit var currentCat: String


    private fun pickRandomWordAndShuffle(): String {
        // Continue picking up a new random word until you get one that hasn't been used before
        currentCat = allCats.random()
        if (usedWords.contains(currentCat)) {
            return pickRandomWordAndShuffle()
        } else {
            usedWords.add(currentCat)
            return shuffleCurrentWord(currentCat)
        }
    }

    private fun shuffleCurrentWord(word: String): String {
        val tempWord = word.toCharArray()
        // Scramble the word
        tempWord.shuffle()
        while (String(tempWord).equals(word)) {
            tempWord.shuffle()
        }
        return String(tempWord)
    }

    /*
    Checks if guess is the same as the currentWord
     */
    fun checkUserGuess() {

        if (userGuess.equals(currentCat, ignoreCase = true)) {
            // User's guess is correct, need to end the game
            // and call updateGameState()
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = false)
            }
        } else {
            // User's guess is wrong, show an error
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = true)
            }
        }
        updateGameState()
        // Reset user guess
        updateUserGuess("")

    }

    /*
    * update the score, increment the current word count and pick a new word
     */
    private fun updateGameState() {
        if (_uiState.value.isGuessedWordWrong == false) {
            //Last round in the game, update isGameOver to true, don't pick a new word
            _uiState.update { currentState ->
                currentState.copy(
                    isGameOver = true
                )
            }
        } else {
            // Normal round in the game
            _uiState.update { currentState ->
                currentState.copy(
                    currentHintCount = currentState.currentHintCount.inc(),
                )
            }
        }
    }

    /* update cat image resource */
    fun updateCurrentCatImage() {
        _uiState.update { currentState ->
            currentState.copy(
                currentCatImage = getDrawableResourceByName(_uiState.value.currentCat),
            )
        }
    }

    fun getDrawableResourceByName(name: String): Int {
        return when (name) {
            "Bb" -> R.drawable.bb
            "Dany" -> R.drawable.dany
            "Thor" -> R.drawable.thor
            "Shelly" -> R.drawable.shelly
            // Add more cases for other drawable names
            else -> throw IllegalArgumentException("Unknown drawable name: $name")
        }




    }





    fun skip() {
        resetGame()
    }
}

