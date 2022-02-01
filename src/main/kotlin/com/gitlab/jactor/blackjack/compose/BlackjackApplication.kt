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

fun main() = application {
    var playerName: String? by remember { mutableStateOf(null) }

    if (playerName == null) {
        Window(
            onCloseRequest = { exitApplication() },
            title = WHAT_NAME,
            state = rememberWindowState(width = 600.dp, height = 300.dp)
        ) {
            playerName = composePlayerNameWindow()
        }
    } else {
        Window(
            onCloseRequest = { exitApplication() },
            title = "Blackjack",
            state = rememberWindowState(width = 900.dp, height = 900.dp)
        ) {
            composeBlackjackWindow(playerName)
        }
    }
}
