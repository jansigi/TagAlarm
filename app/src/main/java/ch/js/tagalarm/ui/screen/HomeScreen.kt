package ch.js.tagalarm.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "TagAlarm",
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Bold,
                    )
                },
                actions = {
                    TextButton(
                        onClick = {
                            // TODO open SettingsScreen
                        },
                    ) {
                        Text(
                            text = "Settings",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                },
            )
        },
    ) { padding ->
        Box(modifier = modifier.padding(padding)) {
            // TODO display Alarms
        }
    }
}
