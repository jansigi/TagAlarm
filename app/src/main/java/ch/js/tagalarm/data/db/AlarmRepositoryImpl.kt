package ch.js.tagalarm.data.db

import ch.js.tagalarm.data.model.Alarm
import ch.js.tagalarm.data.model.NfcTag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AlarmRepositoryImpl(
    private val alarmDao: AlarmDao,
    private val nfcTagDao: NfcTagDao,
) : AlarmRepository {
    override suspend fun getAllAlarms(): List<Alarm> =
        withContext(Dispatchers.IO) {
            alarmDao.getAll().map {
                Alarm(
                    id = it.id,
                    time = it.time,
                    active = it.active,
                    description = it.description,
                    nfcId = it.nfcId,
                )
            }
        }

    override suspend fun getAlarm(id: Long?): Alarm? =
        withContext(Dispatchers.IO) {
            alarmDao.getAlarmEntity(id)?.let {
                Alarm(
                    id = it.id,
                    time = it.time,
                    active = it.active,
                    description = it.description,
                    nfcId = it.nfcId,
                )
            }
        }

    override suspend fun saveAlarm(alarm: Alarm) =
        withContext(Dispatchers.IO) {
            val foundAlarm = alarmDao.getAlarmEntity(alarm.id)
            val alarmEntity = if (alarm.id == null) {
                AlarmEntity(
                    time = alarm.time,
                    active = alarm.active,
                    description = alarm.description,
                    nfcId = alarm.nfcId,
                )
            } else {
                AlarmEntity(
                    id = alarm.id,
                    time = alarm.time,
                    active = alarm.active,
                    description = alarm.description,
                    nfcId = alarm.nfcId,
                )
            }
            if (foundAlarm == null) {
                alarmDao.insertIfNotExists(alarmEntity)
            } else {
                alarmDao.update(alarmEntity)
            }
        }

    override suspend fun deleteAlarm(id: Long?) =
        withContext(Dispatchers.IO) {
            val alarm = getAlarm(id)
            if (alarm != null) {
                alarmDao.delete(
                    AlarmEntity(
                        id = alarm.id!!,
                        time = alarm.time,
                        active = alarm.active,
                        description = alarm.description,
                        nfcId = alarm.nfcId,
                    ),
                )
            }
        }

    override suspend fun saveNfc(nfcTag: NfcTag) =
        withContext(Dispatchers.IO) {
            val nfcTagEntity = NfcTagEntity(id = nfcTag.id)
            nfcTagDao.insertIfNotExists(nfcTagEntity)
        }
}
