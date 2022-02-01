package com.gitlab.jactor.blackjack.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun composePlayerNameWindow(): String? {
    var newName: String? by remember { mutableStateOf(null) }
    var playerName: String? = null

    MaterialTheme {
        Row {
            TextField(
                value = "",
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                label = { Text(Constants.WHAT_NAME) },
                placeholder = { Text(if (newName == null) "Player One" else newName!!) },
                onValueChange = { newValue ->
                    if (newValue == "\n" && newName != null) {
                        playerName = newName!!
                    } else {
                        newName = newValueFrom(newName, newValue)
                    }
                }
            )
        }
    }

    return playerName
}

private fun newValueFrom(newName: String?, newValue: String): String {
    return if (newName != null) {
        "$newName$newValue"
    } else {
        newValue
    }
}
