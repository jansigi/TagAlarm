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
import ch.js.tagalarm.ui.screen.HomeScreen
import ch.js.tagalarm.ui.screen.SettingsScreen
import ch.js.tagalarm.ui.theme.TagAlarmTheme
import kotlinx.serialization.Serializable

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
                        composable<Home> {
                            HomeScreen(modifier = Modifier.padding(innerPadding)) {
                                navController.navigate(Settings)
                            }
                        }
                        composable<Settings> {
                            SettingsScreen(modifier = Modifier.padding(innerPadding)) {
                                navController.navigate(Home)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Serializable
private object Home

@Serializable
private object Settings
