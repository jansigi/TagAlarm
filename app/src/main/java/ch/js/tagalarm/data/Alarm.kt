package ch.js.tagalarm.data

import java.time.LocalTime
import java.util.UUID

data class Alarm(
    val id: UUID = UUID.randomUUID(),
    var time: LocalTime,
    var active: Boolean,
    var nfcTag: NfcTag,
    var description: String = "Alarm",
)
