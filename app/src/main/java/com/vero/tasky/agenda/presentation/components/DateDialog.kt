package com.vero.tasky.agenda.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vero.tasky.R
import java.time.LocalDate

@Composable
fun DateDialog(
    dialogState : MaterialDialogState,
    onDayPicked : (date: LocalDate) -> Unit
) {

    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton(stringResource(id = R.string.ok))
            negativeButton(stringResource(id = R.string.cancel))
        }
    ) {
        datepicker { date ->
            onDayPicked(date)
        }
    }
}