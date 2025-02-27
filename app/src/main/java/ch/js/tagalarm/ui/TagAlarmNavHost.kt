package ch.js.tagalarm.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import ch.js.tagalarm.ui.component.AlarmEditScreen
import ch.js.tagalarm.ui.component.HomeScreen
import ch.js.tagalarm.ui.component.SettingsScreen
import ch.js.tagalarm.viewmodel.AlarmViewModel

enum class Screen(val route: String) {
    HOME("home"),
    SETTINGS("settings"),
    // We’ll allow an optional alarmId query parameter for editing
    ALARM_EDIT("alarmEdit"),
}

@Composable
fun TagAlarmNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val alarmViewModel: AlarmViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.HOME.route,
        modifier = modifier,
    ) {
        composable(Screen.HOME.route) {
            HomeScreen(
                navController = navController,
                alarmViewModel = alarmViewModel,
            )
        }
        composable(Screen.SETTINGS.route) {
            SettingsScreen(navController = navController)
        }
        // ALARM_EDIT route with an optional Long argument "alarmId"
        composable(
            route = Screen.ALARM_EDIT.route + "?alarmId={alarmId}",
            arguments = listOf(
                navArgument("alarmId") {
                    type = NavType.LongType
                    defaultValue = -1L  // If no ID is provided, we use -1
                },
            ),
        ) { backStackEntry ->
            val alarmId = backStackEntry.arguments?.getLong("alarmId")?.takeIf { it != -1L }
            AlarmEditScreen(
                navController = navController,
                alarmViewModel = alarmViewModel,
                alarmId = alarmId,
            )
        }
    }
}
