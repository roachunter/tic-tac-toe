package com.example.tictactoe.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.tictactoe.R
import com.example.tictactoe.domain.GameStatus

@Composable
fun GameStatus(
    isXTurn: Boolean,
    gameStatus: GameStatus,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        targetState = gameStatus to isXTurn,
        transitionSpec = {
            (slideInVertically(initialOffsetY = { it }) + fadeIn()) togetherWith
                    (slideOutVertically(targetOffsetY = { -it }) + fadeOut())
        }
    ) { (gameStatus, isXTurn) ->
        Text(
            text = when {
                gameStatus == GameStatus.XWon -> stringResource(R.string.x_won)
                gameStatus == GameStatus.OWon -> stringResource(R.string.o_won)
                gameStatus == GameStatus.Draw -> stringResource(R.string.draw)
                isXTurn -> stringResource(R.string.x_turn)
                !isXTurn -> stringResource(R.string.o_turn)
                else -> ""
            },
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            modifier = modifier
                .fillMaxWidth()
        )
    }
}