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
                    nfcSerial = it.nfcSerial,
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
                    nfcSerial = it.nfcSerial,
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
                    nfcSerial = alarm.nfcSerial,
                )
            } else {
                AlarmEntity(
                    id = alarm.id,
                    time = alarm.time,
                    active = alarm.active,
                    description = alarm.description,
                    nfcSerial = alarm.nfcSerial,
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
                        nfcSerial = alarm.nfcSerial,
                    ),
                )
            }
        }

    override suspend fun saveNfc(nfcTag: NfcTag) =
        withContext(Dispatchers.IO) {
            val nfcTagEntity = NfcTagEntity(
                serialNumber = nfcTag.serialNumber,
                name = nfcTag.name,
            )
            nfcTagDao.insertIfNotExists(nfcTagEntity)
        }

    override suspend fun getAllNfcTags(): List<NfcTag> =
        withContext(Dispatchers.IO) {
            nfcTagDao.getAll().map {
                NfcTag(
                    serialNumber = it.serialNumber,
                    name = it.name,
                )
            }
        }

    override suspend fun deleteNfcTag(serialNumber: String) =
        withContext(Dispatchers.IO) {
            nfcTagDao.deleteBySerialNumber(serialNumber)
        }
}
