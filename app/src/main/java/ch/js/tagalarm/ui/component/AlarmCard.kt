package ch.js.tagalarm.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ch.js.tagalarm.AlarmNav
import ch.js.tagalarm.data.Alarm
import ch.js.tagalarm.viewmodel.AlarmViewModel

@Composable
fun AlarmCard(alarm: Alarm, navController: NavController, alarmViewModel: AlarmViewModel = viewModel()) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .height(90.dp)
            .clickable {
                navController.navigate(AlarmNav(alarm.id.toString()))
            },
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = alarm.time.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                )
                Switch(alarm.active, onCheckedChange = { alarmViewModel.toggleActive(alarm) })
            }
            Text(alarm.description)
        }
    }
}
