package com.gitlab.jactor.blackjack.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun composePlayerNameWindow(): String? {
    var newName by remember { mutableStateOf("") }
    var playerName: String? by remember { mutableStateOf(null) }

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            TextField(
                value = "",
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                label = { Text(Constants.WHAT_NAME) },
                placeholder = { Text(newName) },
                onValueChange = { newValue ->
                    if (newValue == "\n") {
                        playerName = fetchPlayerName(newName)
                    } else {
                        newName = newValueFrom(newName, newValue)
                    }
                }
            )

            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    playerName = fetchPlayerName(newName)
                }
            ) {
                Text("OK")
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
