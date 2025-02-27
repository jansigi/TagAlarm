package ch.js.tagalarm.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nfc")
data class NfcTagEntity(
    @PrimaryKey
    @ColumnInfo(name = "serial_number")
    val serialNumber: String,
    @ColumnInfo(name = "name")
    val name: String,
)
