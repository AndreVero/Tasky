package com.vero.tasky.agenda.presentation.agendaevent

import android.os.Parcelable
import androidx.annotation.StringRes
import com.vero.tasky.R
import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.model.Attendee
import com.vero.tasky.agenda.presentation.model.ReminderRange
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.util.*
@Parcelize
data class EventDetailsState(
    val agendaItem: AgendaItem.Event = AgendaItem.Event(
        id = UUID.randomUUID().toString(),
        title = "New event",
        description = "New event description",
        time = LocalDateTime.now().plusMinutes(30),
        to = LocalDateTime.now().plusMinutes(40),
        remindAt = LocalDateTime.now().minusMinutes(10),
        isUserEventCreator = true,
        attendees = emptyList(),
        photos = emptyList(),
        host = ""
    ),
    val isGoing : Boolean = true,
    val deletedPhotoKeys: List<String> = emptyList(),
    val isEditableForCreator: Boolean = false,
    val isEditableForAttendee: Boolean = false,
    val isLoading: Boolean = false,
    val isErrorEmail: Boolean = false,
    val isGoingAttendees: List<Attendee> = emptyList(),
    val isNotGoingAttendees: List<Attendee> = emptyList(),
    @StringRes val emailLabel: Int = R.string.email_hint,
    val reminderRange: ReminderRange = ReminderRange.MINUTES_10
) : Parcelable
