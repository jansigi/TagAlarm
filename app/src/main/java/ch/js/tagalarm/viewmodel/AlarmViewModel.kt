package ch.js.tagalarm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.js.tagalarm.data.db.AlarmRepository
import ch.js.tagalarm.data.model.Alarm
import ch.js.tagalarm.data.model.NfcTag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository,
) : ViewModel() {
    private val _alarms = MutableStateFlow(emptyList<Alarm>())
    val alarms = _alarms
        .onStart {
            viewModelScope.launch {
                update()
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleActive(alarm: Alarm) {
        viewModelScope.launch {
            alarmRepository.saveAlarm(alarm.copy(active = !alarm.active))
            update()
        }
    }

    fun removeAlarm(id: Long?) {
        viewModelScope.launch {
            alarmRepository.deleteAlarm(id)
            update()
        }
    }

    fun onNfcTagScanned(serialNumber: String) {
        viewModelScope.launch {
            val matchingAlarms = alarmRepository.getAllAlarms()
                .filter { it.nfcSerial == serialNumber && it.active }
            matchingAlarms.forEach { alarm ->
                alarmRepository.saveAlarm(alarm.copy(active = false))
            }
            update()
        }
    }

    fun registerNfcTag(nfcTag: NfcTag) {
        viewModelScope.launch {
            alarmRepository.saveNfc(nfcTag)
        }
    }

    private suspend fun update() {
        alarmRepository.getAllAlarms().let { allAlarms ->
            _alarms.update { allAlarms }
        }
    }
}
