package com.vero.tasky.agenda.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import com.vero.tasky.R

@Composable
fun AgendaItemTextComponent(
    text: String,
    isEditable: Boolean,
    onEditClick: () -> Unit,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            style = textStyle,
            color = MaterialTheme.colors.primary,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { if (isEditable) onEditClick() }
        )
        if (isEditable) {
            Icon(
                imageVector = Icons.Default.ArrowRight,
                contentDescription = stringResource(id = R.string.edit)
            )
        }
    }
}