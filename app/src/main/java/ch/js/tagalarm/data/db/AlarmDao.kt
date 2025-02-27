package ch.js.tagalarm.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface AlarmDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIfNotExists(alarmEntity: AlarmEntity)

    @Query("SELECT * FROM alarm WHERE id = :id")
    suspend fun getAlarmEntity(id: Long?): AlarmEntity?

    @Query("SELECT * FROM alarm ORDER BY time")
    suspend fun getAll(): List<AlarmEntity>

    @Update
    suspend fun update(alarmEntity: AlarmEntity)

    @Delete
    suspend fun delete(alarmEntity: AlarmEntity)
}
