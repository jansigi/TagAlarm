package ch.js.tagalarm

import android.os.Bundle
import android.os.Vibrator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import ch.js.tagalarm.ui.TagAlarmNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var vibrator: Vibrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        setContent {
            MaterialTheme {
                Surface {
                    TagAlarmNavHost(vibrator = vibrator)
                }
            }
        }
    }
}
