package com.vero.tasky.agenda.presentation.eventdetails.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vero.tasky.R
import com.vero.tasky.agenda.domain.model.Attendee
import com.vero.tasky.agenda.domain.util.UserNameParser
import com.vero.tasky.agenda.presentation.components.ProfileIcon
import com.vero.tasky.ui.theme.Inter500Size14
import com.vero.tasky.ui.theme.profileIcon

@Composable
fun AttendeeListItemComponent(
    attendee: Attendee,
    isCreator: Boolean,
    isEditable: Boolean,
    modifier: Modifier = Modifier,
    onDeleteAttendee: (Attendee) -> Unit
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            ProfileIcon(
                text = UserNameParser.toShortName(attendee.fullName)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = attendee.fullName,
                style = MaterialTheme.typography.Inter500Size14,
                color = MaterialTheme.colors.primary,
            )
        }
        if (isCreator) {
            Text(
                text = stringResource(id = R.string.creator),
                color = MaterialTheme.colors.profileIcon,
                style = MaterialTheme.typography.Inter500Size14,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        } else if (isEditable) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(id = R.string.delete_attendee),
                tint = MaterialTheme.colors.primary,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable { onDeleteAttendee(attendee) }
            )
        }
    }
}