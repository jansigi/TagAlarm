package ch.js.tagalarm.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ch.js.tagalarm.ActiveAlarmActivity
import ch.js.tagalarm.MainActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarmId = intent.getLongExtra("alarm_id", -1L)

        val alarmIntent = Intent(context, ActiveAlarmActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("alarm_id", alarmId)
        }
        context.startActivity(alarmIntent)
    }
}
