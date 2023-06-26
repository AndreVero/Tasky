package com.vero.tasky.agenda.presentation.agendaevent

import android.net.Uri
import com.vero.tasky.agenda.presentation.model.ReminderRange
import java.time.LocalDateTime

sealed interface EventDetailsEvent {
    object ChangeMode: EventDetailsEvent
    class AddPhoto(val uri: Uri) : EventDetailsEvent
    class FromDateTimeChanged(val time: LocalDateTime) : EventDetailsEvent
    class ToDateTimeChanged(val time: LocalDateTime) : EventDetailsEvent
    class ReminderChanged(val reminderRange: ReminderRange) : EventDetailsEvent
    class DeletePhoto(val key: String) : EventDetailsEvent
    class AddAttendee(val email: String) : EventDetailsEvent
    object DeleteEvent : EventDetailsEvent
    object SaveEvent : EventDetailsEvent
    object ChangeIsGoingState : EventDetailsEvent
}