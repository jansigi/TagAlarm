package ch.js.tagalarm.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import ch.js.tagalarm.Home

@Composable
fun SettingsScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    Row(modifier = modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
        Button(onClick = { navController.navigate(Home) }) {
            Text(text = "<-")
        }
        Text(text = "Settings")
    }
    // TODO implement SettingsScreen
}
