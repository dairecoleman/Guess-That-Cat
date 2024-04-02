package com.example.guessthatcat.ui

import com.example.guessthatcat.R

data class GameUiState(
    val currentCat: String = "",
    val currentCatImage: Int = R.drawable.thor,
    val currentHintCount: Int = 1,
    val score: Int = 0,
    val isGuessedWordWrong: Boolean = false,
    val isGameOver: Boolean = false
)
