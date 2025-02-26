package ch.js.tagalarm.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ch.js.tagalarm.Home
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmScreen(modifier: Modifier = Modifier, navController: NavHostController, alarmId: UUID?) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    WithConfirmation(
                        onConfirm = {
                            navController.navigate(Home)
                        },
                    ) { onClick ->
                        TextButton(
                            onClick = {
                                onClick()
                            },
                        ) {
                            Text(
                                text = "cancel",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                    }
                    TextButton(
                        onClick = {
                            // TODO Handle save action
                        },
                    ) {
                        Text(
                            text = "save",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                },
            )
        },
    ) { padding ->
        Box(modifier = modifier.padding(padding)) {
            Text(alarmId.toString())
        }
    }
}
