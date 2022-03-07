package com.github.jactor.blackjack.compose.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import com.github.jactor.blackjack.compose.model.GameOption
import com.github.jactor.blackjack.compose.state.BlackjackState

@Composable
fun GameOfBlackjackWindow(
    blackjackState: BlackjackState,
    newGameOption: (GameOption) -> Unit,
    onCloseRequest: () -> Unit,
    height: Dp,
    width: Dp
) {
    Window(
        onCloseRequest = onCloseRequest,
        title = "Blackjack",
        state = rememberWindowState(width = width, height = height)
    ) {
        BlackjackUI(blackjackState = blackjackState, newGameOption = newGameOption)
    }

}