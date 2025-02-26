package ch.js.tagalarm.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ch.js.tagalarm.AlarmNav
import ch.js.tagalarm.Settings
import ch.js.tagalarm.viewmodel.AlarmViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    alarmViewModel: AlarmViewModel = viewModel(),
) {
    val alarms by alarmViewModel.alarms
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "TagAlarm",
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Bold,
                    )
                },
                actions = {
                    TextButton(
                        onClick = {
                            navController.navigate(Settings)
                        },
                    ) {
                        Text(
                            text = "Settings",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                },
            )
        },
    ) { padding ->
        Box(modifier = modifier.padding(padding)) {
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                alarms.map {
                    AlarmCard(it, navController)
                }
                Button(
                    onClick = {
                        navController.navigate(AlarmNav(""))
                    },
                ) {
                    Text("create")
                }
            }
        }
    }
}
