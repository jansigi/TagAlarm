package ch.js.tagalarm.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ch.js.tagalarm.ui.component.AlarmRingingScreen
import ch.js.tagalarm.ui.component.NfcScanScreen
import ch.js.tagalarm.viewmodel.AlarmViewModel

@Composable
fun ActiveAlarmNavHost(alarmId: Long, onClose: () -> Unit) {
    val navController = rememberNavController()
    val alarmViewModel: AlarmViewModel = hiltViewModel()

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
