package com.vero.tasky.agenda.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vero.tasky.R
import com.vero.tasky.core.presentation.components.ProgressBarText
import com.vero.tasky.ui.theme.Inter600Size16
import com.vero.tasky.ui.theme.headerText

@Composable
fun AgendaItemHeaderComponent(
    text: String,
    isEditable: Boolean,
    onBackClick: () -> Unit,
    onChangeModeClick: () -> Unit,
    onSaveClick: () -> Unit,
    isLoading: Boolean,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = stringResource(id = R.string.close),
            tint = MaterialTheme.colors.headerText,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable { onBackClick() },
        )
        Text(
            text = text,
            style = MaterialTheme.typography.Inter600Size16,
            modifier = Modifier.align(Alignment.Center)
        )
        if (isEditable) {
            ProgressBarText(
                isLoading = isLoading,
                textRes = R.string.save,
                textStyle = MaterialTheme.typography.Inter600Size16,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable { onSaveClick() }
            )
        } else {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = stringResource(id = R.string.edit),
                tint = MaterialTheme.colors.headerText,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable { onChangeModeClick() }
            )
        }
    }
}