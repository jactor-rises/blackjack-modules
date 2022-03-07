package com.github.jactor.blackjack.compose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import com.github.jactor.blackjack.compose.model.GameOption
import com.github.jactor.blackjack.compose.model.PlayerName
import com.github.jactor.blackjack.compose.state.BlackjackState
import com.github.jactor.blackjack.compose.ui.GameOfBlackjackWindow
import com.github.jactor.blackjack.compose.ui.PlayerNameWindow
import kotlinx.coroutines.Dispatchers

fun main() = application {
    var playerName: PlayerName by remember { mutableStateOf(PlayerName(Constants.DEFAULT_PLAYER_NAME)) }
    var gameOption: GameOption by remember { mutableStateOf(GameOption.PLAYER_NAME) }
    val newGameOption = { newGameOption: GameOption -> gameOption = newGameOption }

    if (gameOption == GameOption.PLAYER_NAME) {
        PlayerNameWindow(
            newGameOption = newGameOption,
            newPlayerName =  { newPlayerName: PlayerName -> playerName = newPlayerName },
            onCloseRequest = { exitApplication() },
            width = 400.dp,
            height = 145.dp
        )
    } else {
        val blackjackState = BlackjackState(currentPlayerName = { playerName }, runScope = Dispatchers.Main)

        GameOfBlackjackWindow(
            blackjackState = blackjackState,
            onCloseRequest = { exitApplication() },
            newGameOption = newGameOption,
            width = 800.dp,
            height = 600.dp
        )
    }

    if (gameOption == GameOption.QUIT) {
        exitApplication()
    }
}
