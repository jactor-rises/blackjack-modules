package com.gitlab.jactor.blackjack.compose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.gitlab.jactor.blackjack.compose.Constants.WHAT_NAME
import com.gitlab.jactor.blackjack.compose.model.GameOption
import com.gitlab.jactor.blackjack.compose.model.PlayerName
import com.gitlab.jactor.blackjack.compose.state.BlackjackState
import com.gitlab.jactor.blackjack.compose.ui.BlackjackUI
import com.gitlab.jactor.blackjack.compose.ui.PlayerNameUI
import kotlinx.coroutines.Dispatchers

fun main() = application {
    var playerName: PlayerName by remember { mutableStateOf(PlayerName(Constants.DEFAULT_PLAYER_NAME)) }
    var gameOption: GameOption by remember { mutableStateOf(GameOption.PLAYER_NAME) }
    val newGameOption = { newGameOption: GameOption -> gameOption = newGameOption }

    if (gameOption == GameOption.PLAYER_NAME) {
        Window(
            onCloseRequest = { exitApplication() },
            title = WHAT_NAME,
            state = rememberWindowState(width = 400.dp, height = 145.dp)
        ) {
            PlayerNameUI(newGameOption = newGameOption) { newPlayerName: PlayerName -> playerName = newPlayerName }
        }
    } else {
        val blackjackState = BlackjackState(currentPlayerName = { playerName }, runScope = Dispatchers.Main)

        Window(
            onCloseRequest = { exitApplication() },
            title = "Blackjack",
            state = rememberWindowState(width = 800.dp, height = 600.dp)
        ) {
            BlackjackUI(blackjackState = blackjackState, newGameOption = newGameOption)
        }
    }

    if (gameOption == GameOption.QUIT) {
        exitApplication()
    }
}
