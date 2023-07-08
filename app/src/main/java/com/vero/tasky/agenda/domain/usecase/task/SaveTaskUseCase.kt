package com.vero.tasky.agenda.domain.usecase.task

import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.model.ModificationType
import com.vero.tasky.agenda.domain.repository.TaskRepository

class SaveTaskUseCase(
    private val taskRepository: TaskRepository
) {

    suspend operator fun invoke(
        task: AgendaItem.Task,
        modificationType: ModificationType,
    ) : Result<Unit> {
        return taskRepository.saveTask(
            task = task,
            modificationType = modificationType
        )
    }
}