package ch.js.tagalarm.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ch.js.tagalarm.ui.Screen
import ch.js.tagalarm.viewmodel.AlarmViewModel

@Composable
fun AlarmRingingScreen(
    navController: NavHostController,
    alarmViewModel: AlarmViewModel,
    alarmId: Long,
    onClose: () -> Unit,
) {
    val alarms by alarmViewModel.alarms.collectAsState()
    val nfcTags by alarmViewModel.nfcTags.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Alarm is ringing! id: $alarmId", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))
        val alarmFound = alarms.find { it.id == alarmId }
        if (alarmFound != null) {
            val nfcTag = nfcTags.find { it.serialNumber == alarmFound.nfcSerial }
            Text("Scan Tag: ${nfcTag?.name}\n With serial: ${nfcTag?.serialNumber}", textAlign = TextAlign.Center)

            Button(
                onClick = {
                    navController.navigate(Screen.NFC_SCAN.route + "?alarmId=$alarmId")
                },
            ) {
                Text(text = "Scan Tag")
            }
        } else {
            Button(
                onClick = onClose,
            ) {
                Text(text = "Stop")
            }
        }
    }
}
