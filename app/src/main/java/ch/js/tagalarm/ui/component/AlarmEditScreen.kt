package ch.js.tagalarm.ui.component

import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ch.js.tagalarm.data.model.Alarm
import ch.js.tagalarm.ui.Screen
import ch.js.tagalarm.viewmodel.AlarmViewModel
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
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
    var description by remember { mutableStateOf(existingAlarm?.description.orEmpty()) }
    var selectedNfcTag by remember { mutableStateOf(nfcTagsState.find { existingAlarm?.nfcSerial == it.serialNumber }) }
    var showTimePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (showTimePicker) {
        showTimePickerDialog(
            context = context,
            initialTime = time,
            onTimeSelected = { selectedTime ->
                time = selectedTime
            },
            onDismiss = {
                showTimePicker = false
            },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (existingAlarm != null) "Edit Alarm" else "Create Alarm",
                        fontSize = 30.sp,
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
                            text = "cancel",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }

                    TextButton(
                        onClick = {
                            val alarm = Alarm(
                                id = existingAlarm?.id,
                                time = time,
                                active = true,
                                description = description.ifBlank { "Alarm" },
                                nfcSerial = selectedNfcTag?.serialNumber,
                            )
                            alarmViewModel.saveAlarm(alarm)
                            Toast.makeText(context, "Successfully saved Alarm", Toast.LENGTH_SHORT).show()
                            navController.navigate(Screen.HOME.route)
                        },
                    ) {
                        Text(
                            text = "save",
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
                .padding(start = 30.dp, end = 30.dp)
                .fillMaxSize(),
        ) {
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Pick Time:")
                Spacer(modifier = Modifier.padding(20.dp))
                Button(
                    onClick = { showTimePicker = true },
                ) {
                    Text(text = "$time")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Description:")
            OutlinedTextField(
                value = description,
                placeholder = { Text("Alarm") },
                onValueChange = { description = it },
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Select an NFC Tag:")
            var mExpanded by remember { mutableStateOf(false) }
            Box {
                OutlinedTextField(
                    value = selectedNfcTag?.toString().orEmpty(),
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
                            text = { Text(item.toString()) },
                            onClick = {
                                selectedNfcTag = item
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

            if (existingAlarm != null) {
                Button(
                    onClick = {
                        alarmViewModel.removeAlarm(existingAlarm.id)
                        Toast.makeText(context, "Successfully deleted Alarm", Toast.LENGTH_SHORT).show()
                        navController.navigate(Screen.HOME.route)
                    },
                ) {
                    Text("Delete Alarm")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
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
