package ch.js.tagalarm.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nfc")
data class NfcTagEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "nfc_id")
    val id: Long = 0,
    // TODO add specific fields to save NFC-Tag
)
