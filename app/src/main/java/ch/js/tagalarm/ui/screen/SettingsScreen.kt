package ch.js.tagalarm.ui.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SettingsScreen(modifier: Modifier = Modifier, onBack: () -> Unit) {
    Row(modifier = modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
        Button(onClick = { onBack() }) {
            Text(text = "<-")
        }
        Text(text = "Settings")
    }
    // TODO implement SettingsScreen
}
