package com.vero.tasky.agenda.domain.usecase.reminder

data class ReminderUseCases(
    val saveReminder: SaveReminderUseCase,
    val getReminder: GetReminderUseCase,
    val deleteReminder: DeleteReminderUseCase
)