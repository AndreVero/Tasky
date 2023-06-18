package com.vero.tasky.agenda.presentation.agenda.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.vero.tasky.R

@Composable
fun DeleteAgendaItemDialog(
    onDismissRequest: () -> Unit,
    onYesClick: () -> Unit,
    onNoClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.background,
        text = {
            Text(
                text = stringResource(id = R.string.delete_agenda_item),
                style = MaterialTheme.typography.body1,
                color = Color.White
            )
        },
        buttons = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = onYesClick) {
                    Text(
                        text = stringResource(id = R.string.ok),
                        style = MaterialTheme.typography.body1,
                        color = Color.White
                    )
                }
                Button(onClick = onNoClick) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                        style = MaterialTheme.typography.body1,
                        color = Color.White
                    )
                }
            }
        }
    )
}