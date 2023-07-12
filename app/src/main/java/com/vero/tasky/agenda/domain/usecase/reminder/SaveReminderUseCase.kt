package com.vero.tasky.agenda.domain.usecase.reminder

import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.model.ModificationType
import com.vero.tasky.agenda.domain.repository.ReminderRepository

class SaveReminderUseCase(
    private val reminderRepository: ReminderRepository
) {

    suspend operator fun invoke(
        reminder: AgendaItem.Reminder,
        modificationType: ModificationType,
    ) {
        reminderRepository.saveReminder(
            reminder = reminder,
            modificationType = modificationType
        )
    }
}