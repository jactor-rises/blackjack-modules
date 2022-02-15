package com.gitlab.jactor.blackjack.compose.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gitlab.jactor.blackjack.compose.ApplicationConfiguration
import com.gitlab.jactor.blackjack.compose.dto.Action
import com.gitlab.jactor.blackjack.compose.model.GameOfBlackjack
import com.gitlab.jactor.blackjack.compose.model.PlayerName
import com.gitlab.jactor.blackjack.compose.state.BlackjackState
import com.gitlab.jactor.blackjack.compose.state.Lce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher

internal val ARRANGE_5DP_SPACING = Arrangement.spacedBy(5.dp)

@Composable
@Preview
internal fun BlackjackUI(playerName: PlayerName = PlayerName("Tor Egil"), runScope: MainCoroutineDispatcher = Dispatchers.Main) {
    var blackjackState: BlackjackState? by remember { mutableStateOf(null) }
    var gameState: Lce<GameOfBlackjack>? by remember { mutableStateOf(null) }

    ApplicationConfiguration.loadBlackjackState(
        runScope = runScope,
        blackjackStateConsumer = { loadedState: BlackjackState -> blackjackState = loadedState },
        gameStateConsumer = { newGameState: Lce<GameOfBlackjack> -> gameState = newGameState }
    )

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize().padding(15.dp), verticalArrangement = ARRANGE_5DP_SPACING) {
            Row(modifier = Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = ARRANGE_5DP_SPACING) {
                Text("Hi ${playerName.capitalized}! Magnus challenge you to a game of Blackjack.")
            }

            Row(modifier = Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = ARRANGE_5DP_SPACING) {
                Button(
                    enabled = blackjackState != null,
                    onClick = { blackjackState?.playAutomatic(playerName) }
                ) {
                    Text("Play automatic game of blackjack")
                }

                Button(
                    enabled = blackjackState != null,
                    onClick = { blackjackState?.playManual(Action.START, playerName) }
                ) {
                    Text("Play manual game of blackjack")
                }
            }

            when (gameState) {
                is Lce.Loading -> LoadingUI()
                is Lce.Error -> ErrorUI(gameState as Lce.Error)
                is Lce.Content -> GameOfBlackjackUI((gameState as Lce.Content<GameOfBlackjack>).data, playerName, blackjackState)
            }
        } // end column
    } // end material theme
}

@Composable
private fun LoadingUI() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .defaultMinSize(minWidth = 96.dp, minHeight = 96.dp)
        )
    }
}

@Composable
private fun ErrorUI(fail: Lce.Error) {
    val cause = fail.error::class.simpleName
    val message = fail.error.message?.split("nested exception")?.joinToString(separator = "\nnested exception")

    MaterialTheme {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Something fishy happened!  ¯\\_(ツ)_/¯", textAlign = TextAlign.Left, color = Color.Red)
            Text(text = "$cause: $message", textAlign = TextAlign.Center, color = Color.Red)
        }
    }
}
