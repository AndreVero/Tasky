package com.vero.tasky.agenda.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vero.tasky.R
import com.vero.tasky.agenda.presentation.model.ReminderRange
import com.vero.tasky.ui.theme.Inter400Size16
import com.vero.tasky.ui.theme.onTextFieldIcon
import com.vero.tasky.ui.theme.reminderBackgroundColor

@Composable
fun ReminderComponent(
    reminderRange: ReminderRange,
    isEditable: Boolean,
    onReminderClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .width(35.dp)
                .height(35.dp)
                .background(reminderBackgroundColor)
                .clickable(enabled = isEditable) { onReminderClick() }) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = stringResource(id = R.string.add_visitor),
                    tint = MaterialTheme.colors.onTextFieldIcon,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(id = reminderRange.label),
                style = MaterialTheme.typography.Inter400Size16,
                color = MaterialTheme.colors.primary
            )
        }
        if (isEditable) {
            Icon(
                imageVector = Icons.Default.ArrowRight,
                contentDescription = stringResource(id = R.string.edit),
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}