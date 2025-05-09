package com.example.tictactoe.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tictactoe.domain.CellContent
import com.example.tictactoe.domain.GameStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TicTacToeViewModel : ViewModel() {
    // time for player's turn
    private val turnTime = 10000
    // how often should turn time be updated
    private val tickTime = 16
    // job that holds the timer
    private var turnTimerJob: Job? = null

    // game state flow
    private val _state = MutableStateFlow(TicTacToeState())
    // remaining turn time flow
    private val _remainingTurnTime = MutableStateFlow(turnTime)

    // combining game state and turn time
    // into single flow to share with ui
    val state = combine(
        _state, _remainingTurnTime
    ) { state, remainingTurnTime ->
        state.copy(
            remainingTurnTime = remainingTurnTime.toFloat() / turnTime
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        TicTacToeState()
    )

    // responds to events from ui
    fun onEvent(event: TicTacToeEvent) {
        when (event) {
            is TicTacToeEvent.OnFieldSizePick -> initGame(event.fieldSize)
            TicTacToeEvent.OnBackToSizePick -> backToSizePick()
            is TicTacToeEvent.OnTurn -> processTurn(event.cellIndex)
            TicTacToeEvent.OnReset -> resetGame()
        }
    }

    // initializes game by setting initial game state
    private fun initGame(fieldSize: Int) {
        _state.update {
            TicTacToeState(
                fieldSize = fieldSize,
                field = List(fieldSize * fieldSize) { CellContent.Empty },
                gameStatus = GameStatus.InProgress
            )
        }

        startTurnTimer()
    }

    // resets game field
    private fun resetGame() {
        _state.update {
            it.copy(
                field = List(it.fieldSize * it.fieldSize) { CellContent.Empty },
                gameStatus = GameStatus.InProgress,
                isXTurn = true
            )
        }

        startTurnTimer()
    }

    // drives user back to size picking screen
    private fun backToSizePick() {
        _state.update {
            it.copy(
                gameStatus = GameStatus.SizePicking
            )
        }

        stopTurnTimer()
    }

    private fun processTurn(cellIndex: Int) {
        val currentGameState = _state.value.gameStatus
        if (currentGameState != GameStatus.InProgress) return

        val field = _state.value.field.toMutableList()
        val fieldSize = _state.value.fieldSize
        val isXTurn = _state.value.isXTurn

        field[cellIndex] = if (isXTurn) CellContent.X else CellContent.O

        val turnsLeft = calculateTurnsLeft(field)
        val winner = calculateWinner(fieldSize, field)
        val gameStatus = calculateGameStatus(turnsLeft, winner)

        _state.update {
            it.copy(
                field = field,
                gameStatus = gameStatus,
                isXTurn = !isXTurn,
                xWins = if (gameStatus == GameStatus.XWon) it.xWins + 1 else it.xWins,
                oWins = if (gameStatus == GameStatus.OWon) it.oWins + 1 else it.oWins
            )
        }

        startTurnTimer()
    }

    private fun calculateGameStatus(
        turnsLeft: Int,
        winner: CellContent?
    ): GameStatus {
        if (turnsLeft == 0 && winner == null) return GameStatus.Draw

        return when (winner) {
            CellContent.X -> GameStatus.XWon
            CellContent.O -> GameStatus.OWon
            else -> GameStatus.InProgress
        }
    }

    private fun calculateTurnsLeft(field: List<CellContent>) =
        field.count { it == CellContent.Empty }

    private fun calculateWinner(fieldSize: Int, field: List<CellContent>): CellContent? {
        val winLines = calcWinLines(fieldSize)

        winLines.forEach { line ->
            val cells = line.map { cellIndex ->
                field[cellIndex]
            }

            if (cells[0] != CellContent.Empty && cells.all { it == cells[0] }) {
                return cells[0]
            }
        }

        return null
    }

    // Returns all possible win lines for the given field size
    private fun calcWinLines(fieldSize: Int): List<List<Int>> {
        val lines = mutableListOf<List<Int>>()

        val mainDiagonal = mutableListOf<Int>()
        val secondaryDiagonal = mutableListOf<Int>()

        for (i in 0 until fieldSize) {
            mainDiagonal += i + i * fieldSize
            secondaryDiagonal += (i + 1) * (fieldSize - 1)

            val horizontalLine = mutableListOf<Int>()
            val verticalLine = mutableListOf<Int>()
            for (j in 0 until fieldSize) {
                horizontalLine += (i * fieldSize) + j
                verticalLine += i + (j * fieldSize)
            }

            lines += horizontalLine
            lines += verticalLine
        }

        lines += mainDiagonal
        lines += secondaryDiagonal

        return lines
    }

    private fun stopTurnTimer() {
        turnTimerJob?.cancel()
    }

    private fun startTurnTimer() {
        stopTurnTimer()
        _remainingTurnTime.update { turnTime }

        turnTimerJob = viewModelScope.launch {
            while (_state.value.gameStatus == GameStatus.InProgress) {
                _remainingTurnTime.update {
                    (it - tickTime).coerceAtLeast(0)
                }

                if (_remainingTurnTime.value == 0) {
                    passTurn()
                    _remainingTurnTime.update { turnTime }
                }

                delay(tickTime.toLong())
            }
        }
    }

    private fun passTurn() {
        _state.update {
            it.copy(
                isXTurn = !it.isXTurn
            )
        }
    }
}