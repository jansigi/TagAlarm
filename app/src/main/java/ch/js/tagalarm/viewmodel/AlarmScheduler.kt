package ch.js.tagalarm.viewmodel

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import ch.js.tagalarm.data.model.Alarm
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

object AlarmScheduler {

    /**
     * Schedule the given [alarm] to fire at the next occurrence of its [time].
     * For a simple once-a-day alarm, we schedule for "today" if time is in the future,
     * otherwise for "tomorrow".
     */
    fun scheduleAlarm(context: Context, alarm: Alarm) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val triggerTimeMillis = calculateNextTriggerMillis(alarm.time)

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("alarm_id", alarm.id ?: -1L)
        }

        val requestCode = (alarm.id ?: System.currentTimeMillis()).toInt()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTimeMillis,
                pendingIntent,
            )
        } catch (e: SecurityException) {
            Log.d("SecurityException", e.stackTraceToString())
        }
    }

    /**
     * Cancel the given [alarm] if it was previously scheduled.
     */
    fun cancelAlarm(context: Context, alarm: Alarm) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("alarm_id", alarm.id ?: -1L)
        }

        val requestCode = (alarm.id ?: 0L).toInt()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        alarmManager.cancel(pendingIntent)
    }

    /**
     * Calculates the epoch millis for the next time [alarmTime] occurs (today or tomorrow).
     */
    private fun calculateNextTriggerMillis(alarmTime: LocalTime): Long {
        val now = LocalTime.now()
        val today = LocalDate.now()
        val dateTime: LocalDateTime = if (alarmTime.isAfter(now)) {
            LocalDateTime.of(today, alarmTime)
        } else {
            LocalDateTime.of(today.plusDays(1), alarmTime)
        }
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}
