package com.vero.tasky.agenda.data.mappers

import com.vero.tasky.agenda.data.local.entities.TaskEntity
import com.vero.tasky.agenda.data.remote.network.dto.TaskDto
import com.vero.tasky.agenda.data.util.LocalDateTimeConverter
import com.vero.tasky.agenda.domain.model.AgendaItem

object TaskMapper {

    fun toTaskEntity(taskDto: TaskDto) : TaskEntity {
        return TaskEntity(
            id = taskDto.id,
            title = taskDto.title,
            description = taskDto.description,
            remindAt = taskDto.remindAt,
            time = taskDto.time,
            isDone = taskDto.isDone
        )
    }

    fun toTask(taskEntity: TaskEntity) : AgendaItem.Task {
        return AgendaItem.Task(
            id = taskEntity.id,
            title = taskEntity.title,
            description = taskEntity.description,
            remindAt = LocalDateTimeConverter.longToLocalDateTime(taskEntity.remindAt),
            time = LocalDateTimeConverter.longToLocalDateTime(taskEntity.time),
            isDone = taskEntity.isDone
        )
    }

}