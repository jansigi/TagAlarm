package ch.js.tagalarm

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import ch.js.tagalarm.data.db.AlarmRepository
import ch.js.tagalarm.ui.ActiveAlarmNavHost
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ActiveAlarmActivity : ComponentActivity() {

    @Inject
    lateinit var alarmRepository: AlarmRepository
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        mediaPlayer = MediaPlayer.create(this, android.provider.Settings.System.DEFAULT_ALARM_ALERT_URI)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
        val pattern = longArrayOf(0, 1000, 500)
        vibrator?.vibrate(VibrationEffect.createWaveform(pattern, 0))

        val alarmId = intent.getLongExtra("alarm_id", -1L)

        setContent {
            MaterialTheme {
                Surface {
                    ActiveAlarmNavHost(
                        alarmId = alarmId,
                        onClose = {
                            stopAlarm()
                            stopVibrator()
                            startActivity(Intent(applicationContext, MainActivity::class.java))
                            finish()
                        },
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopVibrator()
        stopAlarm()
    }

    private fun stopAlarm() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun stopVibrator() {
        vibrator?.cancel()
        vibrator = null
    }
}
