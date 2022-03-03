package com.github.jactor.blackjack.compose.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.github.jactor.blackjack.compose.Constants
import com.github.jactor.blackjack.compose.model.GameOption
import com.github.jactor.blackjack.compose.model.PlayerName

@Preview
@Composable
internal fun PlayerNameUI(newGameOption: (GameOption) -> Unit = {}, newPlayerName: (PlayerName) -> Unit = {}) {
    val focusRequester = remember { FocusRequester() }
    var nameState by remember { mutableStateOf(TextFieldValue()) }

    MaterialTheme {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .focusRequester(focusRequester = focusRequester)
                    .onGloballyPositioned { focusRequester.requestFocus() }
                    .padding(end = 16.dp)
                    .weight(1f),
                label = { Text(Constants.WHAT_NAME) },
                leadingIcon = { Icon(Icons.Outlined.Person, "Name") },
                placeholder = { Text(Constants.DEFAULT_PLAYER_NAME) },
                value = nameState,
                onValueChange = { newValue ->
                    if (newValue.text.endsWith('\n')) {
                        newPlayerName.invoke(PlayerName(nameState.text.trim().ifEmpty { Constants.DEFAULT_PLAYER_NAME }))
                        newGameOption.invoke(GameOption.CONTINUE)
                    } else {
                        nameState = newValue
                    }
                }
            )

            Button(
                onClick = {
                    newPlayerName.invoke(PlayerName(nameState.text.trim().ifEmpty { Constants.DEFAULT_PLAYER_NAME }))
                    newGameOption.invoke(GameOption.CONTINUE)
                }
            ) {
                Icon(Icons.Outlined.Send, "OK")
            }
        }
    }
}
