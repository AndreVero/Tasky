package com.vero.tasky.agenda.presentation.agendaevent.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vero.tasky.R
import com.vero.tasky.agenda.domain.model.Attendee
import com.vero.tasky.agenda.presentation.agendaevent.model.Filter

@Composable
fun AttendeesComponent(
    isGoingAttendees: List<Attendee>,
    isNotGoingAttendees: List<Attendee>,
    isEditable: Boolean,
    userId: String,
    modifier: Modifier = Modifier
) {
    var currentFilter by remember { mutableStateOf(Filter.ALL) }

    Spacer(modifier = Modifier.height(32.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Filter.values().forEach {
            VisitorFilterButton(
                filter = it,
                isSelected = it == currentFilter,
                onFilterClick = { filter -> currentFilter = filter })
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    if (isGoingAttendees.isNotEmpty() &&
        (currentFilter == Filter.ALL || currentFilter == Filter.GOING)) {
        AttendeesList(
            label = R.string.going,
            attendees = isGoingAttendees,
            currentUserId = userId,
            isEditable = isEditable
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
    if (isNotGoingAttendees.isNotEmpty() &&
        (currentFilter == Filter.ALL || currentFilter == Filter.NOT_GOING)) {
        AttendeesList(
            label = R.string.not_going,
            attendees = isNotGoingAttendees,
            currentUserId = userId,
            isEditable = isEditable
        )
    }
}