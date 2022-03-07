package com.github.jactor.blackjack.compose.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import com.github.jactor.blackjack.compose.Constants
import com.github.jactor.blackjack.compose.model.GameOption
import com.github.jactor.blackjack.compose.model.PlayerName

@Composable
fun PlayerNameWindow(
    newGameOption: (GameOption) -> Unit,
    newPlayerName: (PlayerName) -> Unit,
    onCloseRequest: () -> Unit,
    height: Dp,
    width: Dp
) {
    Window(
        onCloseRequest = onCloseRequest,
        title = Constants.WHAT_NAME,
        state = rememberWindowState(width = width, height = height),
        alwaysOnTop = true,
    ) {
        PlayerNameUI(newGameOption = newGameOption, newPlayerName)
    }
}
