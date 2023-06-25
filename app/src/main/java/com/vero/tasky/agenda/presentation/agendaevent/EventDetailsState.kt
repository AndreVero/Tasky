package com.vero.tasky.agenda.presentation.agendaevent

import androidx.annotation.StringRes
import com.vero.tasky.R
import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.presentation.agendaevent.model.ReminderRange
import java.time.LocalDateTime
import java.util.*

data class EventDetailsState(
    val agendaItem: AgendaItem.Event = AgendaItem.Event(
        id = UUID.randomUUID().toString(),
        title = "New event",
        description = "New event description",
        time = LocalDateTime.now().plusMinutes(30),
        to = LocalDateTime.now().plusMinutes(40),
        remindAt = LocalDateTime.now().minusMinutes(10),
        isUserEventCreator = false,
        attendees = emptyList(),
        photos = emptyList(),
        host = ""
    ),
    val isGoing : Boolean = true,
    val deletedPhotoKeys: List<String> = emptyList(),
    val isEditable: Boolean = false,
    val isLoading: Boolean = false,
    val isNewEvent: Boolean = true,
    val isErrorEmail: Boolean = false,
    @StringRes val emailLabel: Int = R.string.email_hint,
    val reminderRange: ReminderRange = ReminderRange.MINUTES_10
)
