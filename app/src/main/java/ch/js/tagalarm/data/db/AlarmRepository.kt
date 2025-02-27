package ch.js.tagalarm.data.db

import ch.js.tagalarm.data.model.Alarm
import ch.js.tagalarm.data.model.NfcTag

interface AlarmRepository {
    suspend fun getAllAlarms(): List<Alarm>
    suspend fun getAlarm(id: Long?): Alarm?
    suspend fun saveAlarm(alarm: Alarm)
    suspend fun deleteAlarm(id: Long?)
    suspend fun saveNfc(nfcTag: NfcTag)
}
