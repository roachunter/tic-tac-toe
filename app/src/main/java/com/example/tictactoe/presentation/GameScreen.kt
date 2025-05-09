package com.example.tictactoe.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tictactoe.domain.GameStatus
import com.example.tictactoe.presentation.components.Field
import com.example.tictactoe.presentation.components.GameControlButtons
import com.example.tictactoe.presentation.components.GameStatus
import com.example.tictactoe.presentation.components.WinOverlay
import com.example.tictactoe.presentation.components.WinsPanel

@Composable
fun GameScreen(
    state: TicTacToeState,
    onEvent: (TicTacToeEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            GameStatus(
                isXTurn = state.isXTurn,
                gameStatus = state.gameStatus
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = {
                    state.remainingTurnTime.toFloat()
                },
                drawStopIndicator = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Field(
                fieldSize = state.fieldSize,
                field = state.field,
                onEvent = onEvent,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            WinsPanel(
                xWins = state.xWins,
                oWins = state.oWins,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            GameControlButtons(
                onBackToSizePickClick = {
                    onEvent(TicTacToeEvent.OnBackToSizePick)
                },
                onResetClick = {
                    onEvent(TicTacToeEvent.OnReset)
                }
            )
        }

        var showWinOverlay by remember {
            mutableStateOf(false)
        }

        LaunchedEffect(state.gameStatus) {
            if (
                state.gameStatus in listOf(GameStatus.XWon, GameStatus.OWon, GameStatus.Draw)
            ) {
                showWinOverlay = true
            }
        }

        WinOverlay(
            showOverlay = showWinOverlay,
            gameStatus = state.gameStatus,
            onAnimationEnded = {
                showWinOverlay = false
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}