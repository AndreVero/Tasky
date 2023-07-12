package com.vero.tasky.agenda.domain.usecase

import com.vero.tasky.agenda.domain.usecase.event.DeleteEventUseCase
import com.vero.tasky.agenda.domain.usecase.reminder.DeleteReminderUseCase
import com.vero.tasky.agenda.domain.usecase.task.DeleteTaskUseCase
import com.vero.tasky.agenda.domain.usecase.task.SaveTaskUseCase

data class AgendaUseCases constructor(
    val getAgendaForDay: GetAgendaForDayUseCase,
    val updateAgendaForDay: UpdateAgendaForDayUseCase,
    val deleteTaskUseCase: DeleteTaskUseCase,
    val deleteReminderUseCase: DeleteReminderUseCase,
    val deleteEventUseCase: DeleteEventUseCase,
    val updateTask: SaveTaskUseCase,
)