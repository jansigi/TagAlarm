package ch.js.tagalarm.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ch.js.tagalarm.data.db.AlarmRepository
import ch.js.tagalarm.data.model.Alarm
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmRepository: AlarmRepository

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            // Re-schedule all active alarms
            CoroutineScope(Dispatchers.IO).launch {
                val (activeAlarms, inactiveAlarms) = alarmRepository.getAllAlarms().fold(emptyList<Alarm>() to emptyList<Alarm>()) { acc, alarm ->
                    if (alarm.active) {
                        acc.first + alarm to acc.second
                    } else {
                        acc.first to acc.second + alarm
                    }
                }
                activeAlarms.forEach { alarm ->
                    AlarmScheduler.scheduleAlarm(context, alarm)
                }
                inactiveAlarms.forEach { alarm ->
                    AlarmScheduler.cancelAlarm(context, alarm)
                }
            }
        }
    }
}
