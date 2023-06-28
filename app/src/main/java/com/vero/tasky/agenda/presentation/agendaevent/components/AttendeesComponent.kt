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
    attendees: List<Attendee>,
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
    if (attendees.any { it.isGoing } &&
        (currentFilter == Filter.ALL || currentFilter == Filter.GOING)) {
        AttendeesList(
            label = R.string.going,
            attendees = attendees.filter { it.isGoing },
            currentUserId = userId,
            isEditable = isEditable
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
    if (attendees.any { !it.isGoing } &&
        (currentFilter == Filter.ALL || currentFilter == Filter.NOT_GOING)) {
        AttendeesList(
            label = R.string.not_going,
            attendees = attendees.filter { !it.isGoing },
            currentUserId = userId,
            isEditable = isEditable
        )
    }
}