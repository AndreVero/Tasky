package com.vero.tasky.agenda.presentation.eventdetails

import android.net.Uri
import com.vero.tasky.agenda.domain.model.Attendee
import com.vero.tasky.agenda.presentation.model.ReminderRange
import java.time.LocalDate
import java.time.LocalTime

sealed interface EventDetailsEvent {
    object ChangeMode: EventDetailsEvent
    class CheckModifiedInfo(val title: String?,
                            val description: String?,
                            val deletedPhotoUri: String?): EventDetailsEvent
    class AddPhoto(val uri: Uri) : EventDetailsEvent
    class FromDateChanged(val date: LocalDate) : EventDetailsEvent
    class ToDateChanged(val date: LocalDate) : EventDetailsEvent
    class FromTimeChanged(val time: LocalTime) : EventDetailsEvent
    class ToTimeChanged(val time: LocalTime) : EventDetailsEvent
    class ReminderChanged(val reminderRange: ReminderRange) : EventDetailsEvent
    class DeletePhoto(val key: String) : EventDetailsEvent
    class OnEmailUpdated(val email: String) : EventDetailsEvent
    object AddAttendee: EventDetailsEvent
    object HideAddAttendeeDialog: EventDetailsEvent
    object ShowAttendeeDialog: EventDetailsEvent
    class DeleteAttendee(val attendee: Attendee): EventDetailsEvent
    object ChangePresenceState : EventDetailsEvent
    object SaveEvent : EventDetailsEvent
}