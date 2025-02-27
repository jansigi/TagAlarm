package ch.js.tagalarm.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface NfcTagDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIfNotExists(nfcTag: NfcTagEntity)

    @Query("SELECT * FROM nfc WHERE serial_number = :serialNumber")
    suspend fun getNfcTagEntityBySerialNumber(serialNumber: String): NfcTagEntity?

    @Query("SELECT * FROM nfc")
    suspend fun getAll(): List<NfcTagEntity>

    @Update
    suspend fun update(nfcTag: NfcTagEntity)
}
