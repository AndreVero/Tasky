package com.vero.tasky.agenda.domain.usecase.task

import com.vero.tasky.agenda.domain.repository.TaskRepository

class GetTaskUseCase(
    private val taskRepository: TaskRepository
) {

    operator fun invoke(id: String) = taskRepository.getTask(id)
}