package com.gitlab.jactor.blackjack.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun composeBlackjackWindow(playerName: String?) {
    var count by remember { mutableStateOf(0) }
    val nick by remember { mutableStateOf(playerName?.replace(" ", "-")?.lowercase()) }

    MaterialTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row { Text(text = "Value: $count") }
            Row {
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Button(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {
                            count++
                        },

                        ) {
                        Text(if (count == 0) "Yo $playerName!" else "$playerName clicked $count")
                    }

                    Button(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {
                            count = 0
                        }
                    ) {
                        Text("reset for $nick!")
                    }
                }
            }
        }
    }
}
