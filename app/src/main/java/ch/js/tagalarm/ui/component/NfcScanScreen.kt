package ch.js.tagalarm.ui.component

import android.app.Activity
import android.nfc.NfcAdapter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ch.js.tagalarm.R
import ch.js.tagalarm.viewmodel.AlarmViewModel
import java.util.Locale

@Composable
fun NfcScanScreen(
    navController: NavController,
    alarmViewModel: AlarmViewModel,
) {
    val context = LocalContext.current
    val activity = context as Activity
    val nfcAdapter = remember { NfcAdapter.getDefaultAdapter(activity) }

    var scannedSerial by remember { mutableStateOf<String?>(null) }
    var shouldClose by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        if (nfcAdapter != null) {
            val callback = NfcAdapter.ReaderCallback { tag ->
                val serialNumber = parseTagIdToHex(tag.id)
                alarmViewModel.addNfcTag(
                    name = "Scanned NFC Tag",
                    serialNumber = serialNumber,
                )
                scannedSerial = serialNumber
                shouldClose = true
            }
            nfcAdapter.enableReaderMode(
                activity,
                callback,
                NfcAdapter.FLAG_READER_NFC_A or
                    NfcAdapter.FLAG_READER_NFC_B or
                    NfcAdapter.FLAG_READER_NFC_F or
                    NfcAdapter.FLAG_READER_NFC_V or
                    NfcAdapter.FLAG_READER_NFC_BARCODE or
                    NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
                null,
            )
        }
        onDispose {
            nfcAdapter?.disableReaderMode(activity)
        }
    }

    LaunchedEffect(shouldClose) {
        if (shouldClose) {
            navController.popBackStack()
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        color = Color.Transparent,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .wrapContentSize()
                    .padding(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            ) {
                if (scannedSerial == null) {
                    NfcScanInstructions(
                        onClose = { navController.popBackStack() },
                    )
                } else {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Scanned Serial: $scannedSerial\nSaving & closing...",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NfcScanInstructions(onClose: () -> Unit) {
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

private fun parseTagIdToHex(id: ByteArray?): String =
    id?.joinToString(":") { String.format(Locale.US, "%02X", it) }
        ?: "UNKNOWN"
