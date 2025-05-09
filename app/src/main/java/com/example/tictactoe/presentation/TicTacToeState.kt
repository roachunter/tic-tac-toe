package com.example.tictactoe.presentation

import com.example.tictactoe.domain.CellContent
import com.example.tictactoe.domain.GameStatus

data class TicTacToeState(
    val fieldSize: Int = 0,
    val field: List<CellContent> = emptyList(),
    val gameStatus: GameStatus = GameStatus.SizePicking,
    val isXTurn: Boolean = true,
    val remainingTurnTime: Float =  1f,
    val xWins: Int = 0,
    val oWins: Int = 0
)