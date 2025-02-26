package ch.js.tagalarm.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ch.js.tagalarm.data.Alarm
import ch.js.tagalarm.data.NfcTag
import java.time.LocalTime

class AlarmViewModel : ViewModel() {
    private var _alarms = mutableStateOf(
        listOf(
            // TODO remove and replace with local storage
            Alarm(time = LocalTime.of(6, 30), active = false, nfcTag = NfcTag("1")),
            Alarm(time = LocalTime.of(7, 0), active = true, nfcTag = NfcTag("2")),
        ),
    )

    val alarms: State<List<Alarm>>
        get() = _alarms

    fun toggleActive(alarm: Alarm) {
        _alarms.value = _alarms.value.map {
            if (it.id == alarm.id) {
                alarm.copy(active = !alarm.active)
            } else {
                it
            }
        }
    }
}
