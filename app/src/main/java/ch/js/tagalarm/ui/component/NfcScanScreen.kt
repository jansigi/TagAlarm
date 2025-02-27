package ch.js.tagalarm.ui.component

import android.app.Activity
import android.nfc.NfcAdapter
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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

    // A local state to display a scanned serial number (for user feedback)
    var scannedSerial by remember { mutableStateOf<String?>(null) }
    // Once we’ve scanned and saved, we can close automatically
    var shouldClose by remember { mutableStateOf(false) }

    // Enable NFC reader mode
    DisposableEffect(Unit) {
        if (nfcAdapter != null) {
            val callback = NfcAdapter.ReaderCallback { tag ->
                val bytes = tag.id
                val serialNumber = parseTagIdToHex(bytes)
                // Once we have the serial, we can store it
                // For demonstration, we give it a default name
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
                null
            )
        }
        onDispose {
            nfcAdapter?.disableReaderMode(activity)
        }
    }

    // If we successfully scanned, navigate back
    LaunchedEffect(shouldClose) {
        if (shouldClose) {
            // Give a tiny delay if you want user to see the scanned result
            navController.popBackStack()
        }
    }

    // Simple UI
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(contentAlignment = Alignment.Center) {
            Card(
                modifier = Modifier.wrapContentSize(),
            ) {
                // Basic instructions
                if (scannedSerial == null) {
                    NfcScanInstructions(onClose = { navController.popBackStack() })
                } else {
                    // Show what we scanned (optional)
                    Text(
                        text = "Scanned Serial: $scannedSerial\nSaving & closing...",
                        modifier = Modifier.padding(32.dp)
                    )
                }
            }
        }
    }
}

/**
 * Minimal instructions UI.
 */
@Composable
private fun NfcScanInstructions(onClose: () -> Unit) {
    Box(
        modifier = Modifier.padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Hold Your NFC Tag Near the Phone")
    }
    // Close 'X' button
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopEnd
    ) {
        IconButton(onClick = onClose) {
            Text("close")
//            Icon(
//                painter = painterResource(id = R.drawable.ic_close),
//                contentDescription = "Close NFC Scan"
//            )
        }
    }
}

/**
 * Converts the raw Tag ID bytes to a hex string (e.g., "04:1A:8F:2C").
 */
private fun parseTagIdToHex(id: ByteArray?): String {
    if (id == null) return "UNKNOWN"
    return id.joinToString(":") { String.format(Locale.US, "%02X", it) }
}
