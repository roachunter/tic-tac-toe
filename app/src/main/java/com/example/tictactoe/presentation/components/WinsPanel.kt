package com.example.tictactoe.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

@Composable
fun WinsPanel(
    xWins: Int,
    oWins: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        PlayerIcon(player = "x")

        Text(
            text = buildAnnotatedString {
                val scoreStyle = SpanStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace,
                )

                withStyle(scoreStyle) {
                    append("$xWins")
                }

                withStyle(
                    SpanStyle(
                        baselineShift = BaselineShift(.5f),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    append("  :  ")
                }

                withStyle(scoreStyle) {
                    append("$oWins")
                }
            }
        )

        PlayerIcon(player = "o")
    }
}