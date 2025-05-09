package com.example.tictactoe.presentation

sealed interface TicTacToeEvent {
    data class OnFieldSizePick(val fieldSize: Int): TicTacToeEvent
    data object OnBackToSizePick: TicTacToeEvent
    data class OnTurn(val cellIndex: Int): TicTacToeEvent
    data object OnReset: TicTacToeEvent
}