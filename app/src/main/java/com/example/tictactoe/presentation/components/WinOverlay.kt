package com.example.tictactoe.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.tictactoe.R
import com.example.tictactoe.domain.GameStatus
import kotlinx.coroutines.delay
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.compose.OnParticleSystemUpdateListener
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.PartySystem
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

@Composable
fun WinOverlay(
    showOverlay: Boolean,
    gameStatus: GameStatus,
    onAnimationEnded: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = showOverlay,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = modifier
                .background(
                    color = MaterialTheme.colorScheme.scrim.copy(
                        alpha = .8f
                    )
                )
                .clickable(
                    onClick = {},
                    indication = null,
                    interactionSource = null
                ),
            contentAlignment = Alignment.Center
        ) {
            var playAnimations by remember {
                mutableStateOf(false)
            }

            LaunchedEffect(Unit) {
                playAnimations = true
                if (gameStatus == GameStatus.Draw) {
                    delay(1000)
                    onAnimationEnded()
                }
            }

            AnimatedVisibility(
                visible = playAnimations,
                enter = scaleIn() + fadeIn() + slideInVertically(
                    initialOffsetY = { it }
                ),
                exit = fadeOut()
            ) {
                Text(
                    text = when (gameStatus) {
                        GameStatus.XWon -> stringResource(R.string.x_won)
                        GameStatus.OWon -> stringResource(R.string.o_won)
                        GameStatus.Draw -> stringResource(R.string.draw)
                        else -> ""
                    },
                    color = Color.White,
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            if (gameStatus != GameStatus.Draw && playAnimations) {
                KonfettiView(
                    parties = parade(),
                    updateListener = object : OnParticleSystemUpdateListener {
                        override fun onParticleSystemEnded(
                            system: PartySystem,
                            activeSystems: Int
                        ) {
                            onAnimationEnded()
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

fun parade(): List<Party> {
    val party = Party(
        speed = 20f,
        maxSpeed = 80f,
        damping = 0.9f,
        angle = Angle.TOP + 10,
        spread = 60,
        timeToLive = 1000,
        colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
        emitter = Emitter(duration = 1, TimeUnit.SECONDS).perSecond(300),
        position = Position.Relative(-0.1, 0.8)
    )

    return listOf(
        party,
        party.copy(
            angle = Angle.TOP - 10,
            position = Position.Relative(1.1, 0.8)
        ),
    )
}