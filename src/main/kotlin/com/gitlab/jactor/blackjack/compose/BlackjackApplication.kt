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
import com.gitlab.jactor.blackjack.compose.model.PlayerName
import com.gitlab.jactor.blackjack.compose.ui.BlackjackUI
import com.gitlab.jactor.blackjack.compose.ui.PlayerNameUI
import kotlinx.coroutines.Dispatchers

fun main() = application {
    var playerName: PlayerName? by remember { mutableStateOf(null) }

    if (playerName == null) {
        Window(
            onCloseRequest = { exitApplication() },
            title = WHAT_NAME,
            state = rememberWindowState(width = 400.dp, height = 145.dp)
        ) {
            playerName = PlayerNameUI()
        }
    } else {
        Window(
            onCloseRequest = { exitApplication() },
            title = "Blackjack",
            state = rememberWindowState(width = 800.dp, height = 625.dp)
        ) {
            BlackjackUI(playerName!!, Dispatchers.Main)
        }
    }
}
