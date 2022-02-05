package com.gitlab.jactor.blackjack.compose.display

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gitlab.jactor.blackjack.compose.Constants
import com.gitlab.jactor.blackjack.compose.model.PlayerName

@Composable
internal fun composePlayerName(): PlayerName? {
    var newName by remember { mutableStateOf("") }
    var playerName: PlayerName? by remember { mutableStateOf(null) }

    MaterialTheme {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            TextField(
                value = "",
                modifier = Modifier.padding(end = 16.dp).weight(1f),
                label = { Text(Constants.WHAT_NAME) },
                placeholder = { Text(newName) },
                onValueChange = { newValue ->
                    if (newValue == "\n") {
                        playerName = PlayerName(fetchPlayerName(newName))
                    } else {
                        newName = newValueFrom(newName, newValue)
                    }
                },
                leadingIcon = { Icon(Icons.Filled.Person, "Name") },
            )

            Button(
                onClick = {
                    playerName = PlayerName(fetchPlayerName(newName))
                }
            ) {
                Icon(Icons.Outlined.Send, "OK")
            }
        }
    }

    return playerName
}

private fun fetchPlayerName(newName: String) = newName.ifBlank { "Player One" }

private fun newValueFrom(newName: String?, newValue: String): String {
    return if (newName != null) {
        "$newName$newValue"
    } else {
        newValue
    }
}
