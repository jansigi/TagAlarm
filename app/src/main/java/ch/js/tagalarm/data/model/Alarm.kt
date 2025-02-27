package ch.js.tagalarm.data.model

import java.time.LocalTime

data class Alarm(
    val id: Long?,
    val time: LocalTime,
    val active: Boolean,
    val description: String,
    val nfcId: Long?,
)
