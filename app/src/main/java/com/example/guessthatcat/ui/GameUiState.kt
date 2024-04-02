package com.example.guessthatcat.ui

data class GameUiState(
    val currentCat: String = "",
    val currentWordCount: Int = 1,
    val score: Int = 0,
    val isGuessedWordWrong: Boolean = false,
    val isGameOver: Boolean = false
)
