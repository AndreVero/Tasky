package com.vero.tasky.agenda.presentation.agendaevent.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vero.tasky.agenda.domain.model.Attendee
import com.vero.tasky.ui.theme.Inter500Size16

@Composable
fun AttendeesList(
    @StringRes label: Int,
    attendees: List<Attendee>,
    currentUserId: String,
    isEditable: Boolean,
    modifier: Modifier = Modifier,
    onDeleteAttendee: (Attendee) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = label),
            style = MaterialTheme.typography.Inter500Size16,
            color = MaterialTheme.colors.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        attendees.forEach { 
            AttendeeListItemComponent(
                attendee = it,
                isCreator = currentUserId == it.userId,
                isEditable = isEditable,
                onDeleteAttendee = onDeleteAttendee
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

}