package com.vero.tasky.agenda.domain.usecase.reminder

import com.vero.tasky.agenda.domain.repository.ReminderRepository

class GetReminderUseCase(
    private val reminderRepository: ReminderRepository
) {

    suspend operator fun invoke(id: String) = reminderRepository.getReminder(id)
}