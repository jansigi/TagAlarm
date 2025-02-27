package ch.js.tagalarm.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalTime

@Entity(
    tableName = "alarm",
    foreignKeys = [
        ForeignKey(
            entity = NfcTagEntity::class,
            parentColumns = arrayOf("serial_number"),
            childColumns = arrayOf("nfc_serial"),
            onUpdate = ForeignKey.SET_NULL,
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
)
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var time: LocalTime,
    var active: Boolean,
    @ColumnInfo(defaultValue = "Alarm")
    var description: String,
    @ColumnInfo(name = "nfc_serial")
    var nfcSerial: String?,
)
