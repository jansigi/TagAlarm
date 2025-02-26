package ch.js.tagalarm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ch.js.tagalarm.ui.component.AlarmScreen
import ch.js.tagalarm.ui.component.HomeScreen
import ch.js.tagalarm.ui.component.SettingsScreen
import ch.js.tagalarm.ui.theme.TagAlarmTheme
import kotlinx.serialization.Serializable
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TagAlarmTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Home,
                    ) {
                        val modifier = Modifier.padding(innerPadding)
                        composable<Home> {
                            HomeScreen(modifier = modifier, navController = navController)
                        }
                        composable<Settings> {
                            SettingsScreen(modifier = modifier, navController = navController)
                        }
                        composable<AlarmNav> {
                            val alarmNav = it.toRoute<AlarmNav>()
                            val alarmId = evaluateUuid(alarmNav.alarmId)
                            AlarmScreen(modifier = modifier, navController = navController, alarmId = alarmId)
                        }
                    }
                }
            }
        }
    }
}

@Serializable
object Home

@Serializable
object Settings

@Serializable
class AlarmNav(
    val alarmId: String,
)

private fun evaluateUuid(alarmId: String): UUID? =
    if (alarmId.isEmpty()) {
        null
    } else {
        UUID.fromString(alarmId)
    }
