package com.vero.tasky.agenda.presentation.taskdetails

import android.os.Parcelable
import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.presentation.model.ReminderRange
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.util.*

@Parcelize
data class TaskDetailsState(
    val agendaItem: AgendaItem.Task = AgendaItem.Task(
        id = UUID.randomUUID().toString(),
        title = "New task",
        description = "New task description",
        time = LocalDateTime.now().plusMinutes(30),
        remindAt = LocalDateTime.now().minusMinutes(10),
        isDone = false
    ),
    val isEditable: Boolean = false,
    val isLoading: Boolean = false,
    val reminderRange: ReminderRange = ReminderRange.MINUTES_10
) : Parcelable
