package com.example.tictactoe.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.tictactoe.domain.CellContent

@Composable
fun Cell(
    cellContent: CellContent,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    var fontSize by remember {
        mutableStateOf(0.sp)
    }
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick)
            .onGloballyPositioned {
                fontSize = with(density) {
                    (it.size.height / 2f).toSp()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = cellContent != CellContent.Empty,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            Text(
                text = when (cellContent) {
                    CellContent.Empty -> ""
                    CellContent.X -> "x"
                    CellContent.O -> "o"
                },
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = fontSize,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        }
    }
}