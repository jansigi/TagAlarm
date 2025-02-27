package ch.js.tagalarm.ui.component

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ch.js.tagalarm.Home
import ch.js.tagalarm.data.model.Alarm
import ch.js.tagalarm.viewmodel.AlarmViewModel
import java.time.LocalTime
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmScreen(
    context: Context,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    alarmId: Long?,
) {
    val alarmViewModel: AlarmViewModel = hiltViewModel()
    val alarms by alarmViewModel.alarms.collectAsState()

    val foundAlarm = alarms.find { it.id == alarmId }
    val alarm by remember {
        mutableStateOf(
            foundAlarm ?: Alarm(
                id = alarmId,
                time = LocalTime.of(LocalTime.now().hour, LocalTime.now().minute),
                active = true,
                description = "",
                nfcId = null,
            ),
        )
    }

    var selectedTime by remember { mutableStateOf(alarm.time) }
    val selectedNfcTag by remember { mutableStateOf("Bathroom-Tag") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    WithConfirmation(
                        onConfirm = {
                            navController.navigate(Home)
                        },
                    ) { open ->
                        TextButton(onClick = { open() }) {
                            Text(text = "cancel", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                        }
                    }

                    TextButton(
                        onClick = {
                            alarmViewModel.toggleActive(alarm.copy(time = selectedTime))
                            navController.navigate(Home)
                        },
                    ) {
                        Text(text = "save", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    }
                },
            )
        },
        content = { padding ->
            Column(
                modifier = modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(text = "Time:")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = "Time", fontSize = 20.sp)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.LightGray)
                            .clickable {
                                val calendar = Calendar.getInstance()
                                val timePicker = TimePickerDialog(
                                    context,
                                    { _, hour: Int, minute: Int ->
                                        selectedTime = LocalTime.of(hour, minute)
                                    },
                                    calendar.get(Calendar.HOUR_OF_DAY),
                                    calendar.get(Calendar.MINUTE),
                                    true,
                                )
                                timePicker.show()
                            }
                            .padding(8.dp)
                            .width(80.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = selectedTime.toString(),
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                Text(text = "NFC TAG:")
                ExposedDropdownMenuBox(expanded = false, onExpandedChange = {}) {
                    TextField(
                        value = selectedNfcTag,
                        onValueChange = {},
                        readOnly = true,
                    )
                }
                Button(onClick = { /* Handle Add New NFC Tag */ }) {
                    Text("Add new +")
                }

                if (foundAlarm != null) {
                    WithConfirmation(
                        onConfirm = {
                            alarmViewModel.removeAlarm(foundAlarm.id)
                            navController.navigate(Home)
                        },
                    ) { open ->
                        Button(
                            onClick = {
                                open()
                            },
                        ) {
                            Text("Delete")
                        }
                    }
                }
            }
        },
    )
}
