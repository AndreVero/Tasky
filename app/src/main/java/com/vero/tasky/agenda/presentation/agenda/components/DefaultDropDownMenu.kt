package com.vero.tasky.agenda.presentation.agenda.components

import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.vero.tasky.ui.theme.Inter400Size16
import com.vero.tasky.ui.theme.text

@Composable
fun DefaultDropDownMenu(
    actions: HashMap<Int, () -> Unit>,
    onDismissRequest: () -> Unit
) {
    DropdownMenu(
        expanded = true,
        onDismissRequest = {
           onDismissRequest()
        },
    ) {
        actions.forEach { action ->
            DropdownMenuItem(
                onClick = {
                    action.value()
                    onDismissRequest()
                }) {
                Text(
                    text = stringResource(id = action.key),
                    style = MaterialTheme.typography.Inter400Size16,
                    color = MaterialTheme.colors.text
                )
            }
        }
    }
}