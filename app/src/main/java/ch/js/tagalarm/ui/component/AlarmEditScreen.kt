package ch.js.tagalarm.ui.component

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ch.js.tagalarm.data.model.Alarm
import ch.js.tagalarm.ui.Screen
import ch.js.tagalarm.viewmodel.AlarmViewModel
import java.time.LocalTime

@Composable
fun AlarmEditScreen(
    navController: NavController,
    alarmViewModel: AlarmViewModel,
    alarmId: Long? = null,
) {
    // Observe all alarms so we can find the existing one (if alarmId != null)
    val alarmsState by alarmViewModel.alarms.collectAsState()
    val existingAlarm = alarmsState.find { it.id == alarmId }

    // We store the current context for launching TimePickerDialog
    val context = LocalContext.current

    // Initialize UI state from the existing alarm if present, else defaults
    var time by remember { mutableStateOf(existingAlarm?.time ?: LocalTime.of(7, 0)) }
    var active by remember { mutableStateOf(existingAlarm?.active ?: false) }
    var description by remember { mutableStateOf(existingAlarm?.description ?: "New Alarm") }
    var nfcSerial by remember { mutableStateOf(existingAlarm?.nfcSerial ?: "") }

    // Show/hide the time picker dialog
    var showTimePicker by remember { mutableStateOf(false) }
    if (showTimePicker) {
        showTimePickerDialog(
            context = context,
            initialTime = time,
            onTimeSelected = { selectedTime ->
                time = selectedTime
            },
            onDismiss = { showTimePicker = false },
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(text = if (existingAlarm != null) "Edit Alarm" else "Create Alarm")

        Spacer(modifier = Modifier.height(16.dp))

        // Time Picker
        Button(
            onClick = { showTimePicker = true },
        ) {
            Text(text = "Pick Time: $time")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = nfcSerial,
            onValueChange = { nfcSerial = it },
            label = { Text("NFC Tag Serial (Optional)") },
        )

        Spacer(modifier = Modifier.height(8.dp))

        Switch(
            checked = active,
            onCheckedChange = { active = it },
        )
        Text(text = if (active) "Active" else "Inactive")

        Spacer(modifier = Modifier.height(16.dp))

        // Save button
        Button(
            onClick = {
                // Build the Alarm object
                val alarm = Alarm(
                    id = existingAlarm?.id, // If this is an edit, reuse the ID
                    time = time,
                    active = active,
                    description = description,
                    nfcSerial = if (nfcSerial.isNotBlank()) nfcSerial else null,
                )
                alarmViewModel.saveAlarm(alarm)
                navController.navigate(Screen.HOME.route)
            },
        ) {
            Text("Save Alarm")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Delete button if editing an existing alarm
        if (existingAlarm != null) {
            Button(
                onClick = {
                    alarmViewModel.removeAlarm(existingAlarm.id)
                    navController.navigate(Screen.HOME.route)
                },
            ) {
                Text("Delete Alarm")
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(onClick = { navController.navigate(Screen.HOME.route) }) {
            Text("Cancel")
        }
    }
}

/**
 * Helper function to show a TimePickerDialog from within a Compose context.
 */
private fun showTimePickerDialog(
    context: Context,
    initialTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit,
    onDismiss: () -> Unit,
) {
    val hour = initialTime.hour
    val minute = initialTime.minute

    val dialog = TimePickerDialog(
        context,
        { _, selectedHour, selectedMinute ->
            onTimeSelected(LocalTime.of(selectedHour, selectedMinute))
            onDismiss()
        },
        hour,
        minute,
        true, // 24-hour format
    )
    dialog.setOnCancelListener { onDismiss() }
    dialog.show()
}
