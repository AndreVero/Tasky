package com.vero.tasky.agenda.domain.usecase.task

data class TaskUseCases(
    val saveTask: SaveTaskUseCase,
    val getTask: GetTaskUseCase,
    val deleteTask: DeleteTaskUseCase
)