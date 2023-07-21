package com.vero.tasky.agenda.presentation.reminderdetails

import android.os.Parcelable
import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.presentation.model.ReminderRange
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.util.*

@Parcelize
data class ReminderDetailsState(
    val agendaItem: AgendaItem.Reminder = AgendaItem.Reminder(
        id = UUID.randomUUID().toString(),
        title = "New reminder",
        description = "New reminder description",
        time = LocalDateTime.now().plusMinutes(30),
        remindAt = LocalDateTime.now().plusMinutes(20)
    ),
    val isEditable: Boolean = false,
    val isLoading: Boolean = false,
    val reminderRange: ReminderRange = ReminderRange.MINUTES_10
) : Parcelable
