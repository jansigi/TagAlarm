package ch.js.tagalarm.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun WithConfirmation(
    title: String = "Please Confirm",
    message: String = "Are you sure you want to perform this action?",
    confirmText: String = "Confirm",
    cancelText: String = "Dismiss",
    onConfirm: () -> Unit,
    content: @Composable (onClick: () -> Unit) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    content { showDialog = true }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = title) },
            text = { Text(text = message) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        onConfirm()
                    },
                ) {
                    Text(confirmText)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false },
                ) {
                    Text(cancelText)
                }
            },
        )
    }
}
