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
import java.util.UUID

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

        // List all tags
        LazyColumn {
            items(nfcTags) { tag ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                ) {
                    // Show the name or serial
                    Text(
                        text = tag.name,
                        modifier = Modifier.weight(1f),
                    )
                    // Show the serial number
                    Text(text = tag.serialNumber)

                    // Delete button
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

        // "Add new +" button
        Button(
            onClick = {
                // Placeholder scanning approach:
                // In a real app, you'd launch an NFC scan and retrieve the actual serialNumber
                val fakeSerial = "FAKE-${UUID.randomUUID()}"
                alarmViewModel.addNfcTag(name = "Bathroom-Tag", serialNumber = fakeSerial)
            },
        ) {
            Text(text = "Add new +")
        }
    }
}
