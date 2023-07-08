package com.vero.tasky.agenda.data.mappers

import com.vero.tasky.agenda.data.local.entities.TaskEntity
import com.vero.tasky.agenda.data.remote.network.dto.TaskDto
import com.vero.tasky.agenda.data.remote.network.request.TaskRequest
import com.vero.tasky.agenda.data.util.LocalDateTimeConverter
import com.vero.tasky.agenda.domain.model.AgendaItem

fun TaskDto.toTaskEntity(): TaskEntity {
    return TaskEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        remindAt = this.remindAt,
        time = this.time,
        isDone = this.isDone
    )
}

fun TaskEntity.toTask(): AgendaItem.Task {
    return AgendaItem.Task(
        id = this.id,
        title = this.title,
        description = this.description,
        remindAt = LocalDateTimeConverter.longToLocalDateTime(this.remindAt),
        time = LocalDateTimeConverter.longToLocalDateTime(this.time),
        isDone = this.isDone
    )
}

fun TaskEntity.toTaskRequest(): TaskRequest {
    return TaskRequest(
        id = this.id,
        title = this.title,
        description = this.description,
        remindAt = this.remindAt,
        time = this.time,
        isDone = this.isDone
    )
}

fun AgendaItem.Task.toTaskEntity(): TaskEntity {
    return TaskEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        remindAt = LocalDateTimeConverter.localDateTimeToLong(this.remindAt),
        time = LocalDateTimeConverter.localDateTimeToLong(this.time),
        isDone = this.isDone
    )
}

