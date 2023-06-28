package com.vero.tasky.agenda.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vero.tasky.R
import com.vero.tasky.ui.theme.Inter400Size16

@Composable
fun TimePickerLabelComponent(
    @StringRes label: Int,
    value: String,
    isEditable: Boolean,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier
        .width(160.dp)
        .clickable(enabled = isEditable) { onEditClick() }) {
        Text(
            text = stringResource(id = label),
            style = MaterialTheme.typography.Inter400Size16,
            color = MaterialTheme.colors.primary,
            modifier = modifier.align(Alignment.CenterStart)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.Inter400Size16,
            color = MaterialTheme.colors.primary,
            modifier = modifier.align(Alignment.Center)
        )
        if (isEditable) {
            Icon(
                imageVector = Icons.Default.ArrowRight,
                contentDescription = stringResource(id = R.string.edit),
                modifier = modifier.align(Alignment.CenterEnd),
                tint = MaterialTheme.colors.primary
            )
        }
    }
}