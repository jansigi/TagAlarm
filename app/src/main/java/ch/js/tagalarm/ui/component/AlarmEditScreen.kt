package ch.js.tagalarm.ui.component

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    val alarmsState by alarmViewModel.alarms.collectAsState()
    val nfcTagsState by alarmViewModel.nfcTags.collectAsState()

    val existingAlarm = alarmsState.find { it.id == alarmId }

    var time by remember { mutableStateOf(existingAlarm?.time ?: LocalTime.of(LocalTime.now().hour, LocalTime.now().minute)) }
    var description by remember { mutableStateOf(existingAlarm?.description ?: "New Alarm") }
    var selectedNfcTagSerial by remember { mutableStateOf(existingAlarm?.nfcSerial ?: "") }
    var showTimePicker by remember { mutableStateOf(false) }

    if (showTimePicker) {
        showTimePickerDialog(
            context = LocalContext.current,
            initialTime = time,
            onTimeSelected = { selectedTime ->
                time = selectedTime
            },
            onDismiss = {
                showTimePicker = false
            },
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(text = if (existingAlarm != null) "Edit Alarm" else "Create Alarm")

        Spacer(modifier = Modifier.height(16.dp))

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

        Text(text = "Select an NFC Tag:")
        var mExpanded by remember { mutableStateOf(false) }
        Box {
            OutlinedTextField(
                value = selectedNfcTagSerial,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { mExpanded = true },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown Arrow",
                        modifier = Modifier.clickable { mExpanded = true },
                    )
                },
            )

            DropdownMenu(
                expanded = mExpanded,
                onDismissRequest = { mExpanded = false },
                modifier = Modifier.fillMaxWidth(),
            ) {
                nfcTagsState.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item.name + item.serialNumber) },
                        onClick = {
                            selectedNfcTagSerial = item.serialNumber
                            mExpanded = false
                        },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                navController.navigate(Screen.NFC_SCAN.route)
            },
        ) {
            Text("Scan New Tag")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val alarm = Alarm(
                    id = existingAlarm?.id,
                    time = time,
                    active = true,
                    description = description,
                    nfcSerial = selectedNfcTagSerial.ifBlank { null },
                )
                alarmViewModel.saveAlarm(alarm)
                navController.navigate(Screen.HOME.route)
            },
        ) {
            Text("Save Alarm")
        }

        Spacer(modifier = Modifier.height(8.dp))

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
        true,
    )
    dialog.setOnCancelListener { onDismiss() }
    dialog.show()
}
