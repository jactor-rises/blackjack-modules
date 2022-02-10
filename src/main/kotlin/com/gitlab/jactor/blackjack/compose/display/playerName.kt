package com.gitlab.jactor.blackjack.compose.display

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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.gitlab.jactor.blackjack.compose.Constants
import com.gitlab.jactor.blackjack.compose.model.PlayerName

@Preview
@Composable
internal fun composePlayerName(): PlayerName? {
    var nameState by remember { mutableStateOf(TextFieldValue()) }
    var playerName: PlayerName? by remember { mutableStateOf(null) }

    MaterialTheme {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = nameState,
                modifier = Modifier.padding(end = 16.dp).weight(1f),
                label = { Text(Constants.WHAT_NAME) },
                leadingIcon = { Icon(Icons.Outlined.Person, "Name") },
                placeholder = { Text(Constants.DEFAULT_PLAYER_NAME) },
                onValueChange = { newValue ->
                    if (newValue.text.endsWith('\n')) {
                        playerName = PlayerName(nameState.text.trim())
                    } else {
                        nameState = newValue
                    }
                }
            )

            Button(
                onClick = {
                    playerName = PlayerName(nameState.text.ifBlank { Constants.DEFAULT_PLAYER_NAME })
                }
            ) {
                Icon(Icons.Outlined.Send, "OK")
            }
        }
    }

    return playerName
}
