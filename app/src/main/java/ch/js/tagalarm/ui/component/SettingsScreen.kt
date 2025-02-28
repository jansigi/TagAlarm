package ch.js.tagalarm.ui.component

import android.widget.Toast
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ch.js.tagalarm.ui.Screen
import ch.js.tagalarm.viewmodel.AlarmViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    alarmViewModel: AlarmViewModel,
) {
    val nfcTags by alarmViewModel.nfcTags.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                    )
                },
                actions = {
                    TextButton(
                        onClick = {
                            navController.navigate(Screen.HOME.route)
                        },
                    ) {
                        Text(
                            text = "back",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(start = 30.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "NFC",
                    fontSize = 20.sp,
                )
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
                        Text(text = tag.toString())
                        IconButton(
                            onClick = {
                                alarmViewModel.deleteNfcTag(tag.serialNumber)
                                Toast.makeText(context, "Successfully deleted Tag", Toast.LENGTH_SHORT).show()
                            },
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Tag")
                        }
                    }
                }
                if (nfcTags.isEmpty()) {
                    item {
                        Text(text = "No Tag yet.")
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
}
