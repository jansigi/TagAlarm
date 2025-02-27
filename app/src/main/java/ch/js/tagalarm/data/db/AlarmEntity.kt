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
            parentColumns = arrayOf("nfc_id"),
            childColumns = arrayOf("nfc_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
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
    @ColumnInfo(name = "nfc_id")
    var nfcId: Long?,
)
