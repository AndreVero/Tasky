package com.vero.tasky.agenda.presentation.taskdetails

import com.vero.tasky.agenda.presentation.model.ReminderRange
import java.time.LocalDate
import java.time.LocalTime

sealed interface TaskDetailsEvent {
    object ChangeMode: TaskDetailsEvent
    class CheckModifiedInfo(val title: String?,
                            val description: String?): TaskDetailsEvent
    class AtDateChanged(val date: LocalDate) : TaskDetailsEvent
    class AtTimeChanged(val time: LocalTime) : TaskDetailsEvent
    class ReminderChanged(val reminderRange: ReminderRange) : TaskDetailsEvent
    object SaveEvent : TaskDetailsEvent
    object DeleteEvent : TaskDetailsEvent
}