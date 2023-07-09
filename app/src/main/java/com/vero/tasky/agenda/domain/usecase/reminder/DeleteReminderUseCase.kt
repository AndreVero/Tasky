package com.vero.tasky.agenda.domain.usecase.reminder

import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.repository.ReminderRepository

class DeleteReminderUseCase(
    private val reminderRepository: ReminderRepository
) {

    suspend operator fun invoke(reminder: AgendaItem.Reminder) =
        reminderRepository.deleteReminder(reminder)
}