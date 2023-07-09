package com.vero.tasky.agenda.domain.repository

import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.model.ModificationType

interface ReminderRepository {

    suspend fun saveReminder(
        reminder: AgendaItem.Reminder,
        modificationType: ModificationType
    ) : Result<Unit>

    suspend fun getReminder(id: String):  AgendaItem.Reminder

    suspend fun deleteReminder(reminder: AgendaItem.Reminder)
}