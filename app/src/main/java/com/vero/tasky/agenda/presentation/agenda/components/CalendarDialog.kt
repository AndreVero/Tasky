package com.vero.tasky.agenda.presentation.agenda.components

import androidx.compose.runtime.Composable
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import java.time.LocalDate

@Composable
fun CalendarDialog(
    dialogState : MaterialDialogState,
    onDayPicked : (date: LocalDate) -> Unit
) {

    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        }
    ) {
        datepicker { date ->
            onDayPicked(date)
        }
    }
}