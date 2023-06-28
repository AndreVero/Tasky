package com.vero.tasky.agenda.presentation.agendaevent.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vero.tasky.R
import com.vero.tasky.ui.theme.Inter700Size20
import com.vero.tasky.ui.theme.onTextFieldIcon
import com.vero.tasky.ui.theme.reminderBackgroundColor

@Composable
fun VisitorsLabelComponent(
    onAddVisitorClick: () -> Unit,
    isEditable: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = R.string.visitors),
            style = MaterialTheme.typography.Inter700Size20,
            color = MaterialTheme.colors.primary
        )
        Spacer(modifier = Modifier.width(12.dp))
        if (isEditable) {
            Box(modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .width(35.dp)
                .height(35.dp)
                .background(reminderBackgroundColor)
                .clickable { onAddVisitorClick() }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_visitor),
                    tint = MaterialTheme.colors.onTextFieldIcon,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}