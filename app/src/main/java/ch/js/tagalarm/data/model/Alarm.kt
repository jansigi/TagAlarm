package ch.js.tagalarm.data.model

import java.time.LocalTime

data class Alarm(
    val id: Long? = null,
    val time: LocalTime,
    val active: Boolean,
    val description: String = "Alarm",
    val nfcSerial: String? = null,
)
