package ch.js.tagalarm.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.js.tagalarm.R

@Composable
fun NfcScanInstructions(onClose: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        IconButton(
            onClick = onClose,
            modifier = Modifier.align(Alignment.TopEnd),
        ) {
            Text("X")
        }
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Ready to Scan",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp),
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Hold Your NFC Tag\nNear the Phone",
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
            )
            Spacer(Modifier.height(20.dp))
            Icon(
                painter = painterResource(id = R.drawable.nfc_scan),
                contentDescription = "NFC Scan",
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onClose, modifier = Modifier.fillMaxWidth()) {
                Text(text = "cancel", fontSize = 20.sp)
            }
        }
    }
}
