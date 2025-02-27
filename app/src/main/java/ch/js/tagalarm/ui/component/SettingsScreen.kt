package ch.js.tagalarm.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ch.js.tagalarm.ui.Screen
import ch.js.tagalarm.viewmodel.AlarmViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    alarmViewModel: AlarmViewModel,
) {
    val nfcTags by alarmViewModel.nfcTags.collectAsState()

    Column(
        modifier = Modifier
            .padding(16.dp),
    ) {
        // Top row: "NFC" title + Back button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "NFC",
                modifier = Modifier.weight(1f),
            )
            Button(onClick = { navController.navigate(Screen.HOME.route) }) {
                Text(text = "Back")
            }
        }

        Spacer(modifier = Modifier.padding(8.dp))

        LazyColumn {
            items(nfcTags) { tag ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                ) {
                    Text(
                        text = tag.name,
                        modifier = Modifier.weight(1f),
                    )
                    Text(text = tag.serialNumber)
                    IconButton(
                        onClick = {
                            alarmViewModel.deleteNfcTag(tag.serialNumber)
                        },
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Tag")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.padding(8.dp))

        Button(
            onClick = {
                navController.navigate(Screen.NFC_SCAN.route)
            },
        ) {
            Text(text = "Scan New Tag")
        }
    }
}
