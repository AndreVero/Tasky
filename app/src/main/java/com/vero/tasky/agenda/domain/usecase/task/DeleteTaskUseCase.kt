package com.vero.tasky.agenda.domain.usecase.task

import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.repository.TaskRepository

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository
) {

    suspend operator fun invoke(task: AgendaItem.Task) = taskRepository.deleteTask(task)
}