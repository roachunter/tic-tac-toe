package com.example.tictactoe.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tictactoe.domain.CellContent
import com.example.tictactoe.presentation.TicTacToeEvent

@Composable
fun Field(
    fieldSize: Int,
    field: List<CellContent>,
    onEvent: (TicTacToeEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = 8.dp
    Column(
        modifier = modifier
            .aspectRatio(1f)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.large
            )
            .padding(spacing),
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        field.chunked(fieldSize).forEachIndexed { rowIndex, row ->
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(spacing)
            ) {
                row.forEachIndexed { cellIndex, cell ->
                    Cell(
                        cellContent = cell,
                        onClick = {
                            onEvent(TicTacToeEvent.OnTurn(rowIndex * fieldSize + cellIndex))
                        },
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = MaterialTheme.shapes.medium
                            )
                    )
                }
            }
        }
    }
}