package ch.js.tagalarm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
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
    application: Application,
) : AndroidViewModel(application) {
    private val _alarms = MutableStateFlow(emptyList<Alarm>())
    private val _nfcTags = MutableStateFlow(emptyList<NfcTag>())

    val nfcTags = _nfcTags
        .onStart {
            viewModelScope.launch {
                loadAllNfcTags()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val alarms = _alarms
        .onStart {
            viewModelScope.launch {
                updateAlarms()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleActive(alarm: Alarm) {
        viewModelScope.launch {
            alarmRepository.saveAlarm(alarm.copy(active = !alarm.active))
            updateAlarms()
        }
    }

    fun removeAlarm(id: Long?) {
        viewModelScope.launch {
            alarmRepository.deleteAlarm(id)
            updateAlarms()
        }
    }

    fun saveAlarm(alarm: Alarm) {
        viewModelScope.launch {
            alarmRepository.saveAlarm(alarm)
            updateAlarms()

            if (alarm.active) {
                AlarmScheduler.scheduleAlarm(getApplication(), alarm)
            } else {
                AlarmScheduler.cancelAlarm(getApplication(), alarm)
            }
        }
    }

    suspend fun loadAllNfcTags() {
        alarmRepository.getAllNfcTags().let { allNfcTags ->
            _nfcTags.update { allNfcTags }
        }
    }

    fun saveNfcTag(name: String, serialNumber: String) {
        viewModelScope.launch {
            alarmRepository.saveNfc(NfcTag(serialNumber, name))
            loadAllNfcTags()
        }
    }

    fun deleteNfcTag(serialNumber: String) {
        viewModelScope.launch {
            alarmRepository.deleteNfcTag(serialNumber)
            loadAllNfcTags()
        }
    }

    private suspend fun updateAlarms() {
        alarmRepository.getAllAlarms().let { allAlarms ->
            _alarms.update { allAlarms }
        }
    }
}
