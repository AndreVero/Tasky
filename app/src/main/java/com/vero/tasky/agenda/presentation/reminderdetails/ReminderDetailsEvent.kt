package com.vero.tasky.agenda.presentation.reminderdetails

import com.vero.tasky.agenda.presentation.model.ReminderRange
import java.time.LocalDate
import java.time.LocalTime

sealed interface ReminderDetailsEvent {
    object ChangeMode: ReminderDetailsEvent
    class CheckModifiedInfo(val title: String?,
                            val description: String?): ReminderDetailsEvent
    class AtDateChanged(val date: LocalDate) : ReminderDetailsEvent
    class AtTimeChanged(val time: LocalTime) : ReminderDetailsEvent
    class ReminderChanged(val reminderRange: ReminderRange) : ReminderDetailsEvent
    object SaveReminder : ReminderDetailsEvent
    object DeleteReminder : ReminderDetailsEvent
}