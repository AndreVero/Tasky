package com.vero.tasky.agenda.presentation.components

import androidx.compose.runtime.Composable
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import java.time.LocalTime

@Composable
fun TimeDialog(
    dialogState : MaterialDialogState,
    onTimePicked : (time: LocalTime) -> Unit,
) {

    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        },
    ) {
        timepicker { time ->
            onTimePicked(time)
        }
    }
}