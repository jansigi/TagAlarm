package ch.js.tagalarm

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ch.js.tagalarm.data.db.AlarmRepository
import ch.js.tagalarm.ui.Screen
import ch.js.tagalarm.ui.component.AlarmRingingScreen
import ch.js.tagalarm.ui.component.NfcScanScreen
import ch.js.tagalarm.viewmodel.AlarmViewModel
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
            val navController = rememberNavController()
            val alarmViewModel: AlarmViewModel = hiltViewModel()

            val onClose = {
                stopAlarm()
                stopVibrator()
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }

            NavHost(
                navController = navController,
                startDestination = Screen.ACTIVE_ALARM.route,
            ) {
                composable(Screen.ACTIVE_ALARM.route) {
                    AlarmRingingScreen(
                        navController = navController,
                        alarmViewModel = alarmViewModel,
                        alarmId = alarmId,
                        onClose = onClose,
                    )
                }

                composable(
                    route = Screen.NFC_SCAN.route + "?alarmId={alarmId}",
                    arguments = listOf(
                        navArgument("alarmId") {
                            type = NavType.LongType
                            defaultValue = -1L
                        },
                    ),
                ) { backStackEntry ->
                    val alarmIdArgument = backStackEntry.arguments?.getLong("alarmId")
                    NfcScanScreen(
                        navController = navController,
                        alarmViewModel = alarmViewModel,
                        alarmId = alarmIdArgument,
                        onClose = onClose,
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

