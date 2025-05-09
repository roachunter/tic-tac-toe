package com.example.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.tictactoe.domain.GameStatus
import com.example.tictactoe.presentation.GameScreen
import com.example.tictactoe.presentation.TicTacToeEvent
import com.example.tictactoe.presentation.TicTacToeViewModel
import com.example.tictactoe.presentation.WelcomeScreen
import com.example.tictactoe.ui.theme.TicTacToeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicTacToeTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = MaterialTheme.colorScheme.background
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val viewModel by viewModels<TicTacToeViewModel>()
                    val state by viewModel.state.collectAsState()

                    AnimatedContent(
                        targetState = state.gameStatus != GameStatus.SizePicking
                    ) { isSizePicked ->
                        when (isSizePicked) {
                            false -> {
                                WelcomeScreen(
                                    onSizePicked = {
                                        viewModel.onEvent(TicTacToeEvent.OnFieldSizePick(it))
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            true -> {
                                GameScreen(
                                    state = state,
                                    onEvent = viewModel::onEvent,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
