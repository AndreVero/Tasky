package com.vero.tasky.agenda.data.mappers

import com.vero.tasky.agenda.data.local.entities.ReminderEntity
import com.vero.tasky.agenda.data.remote.network.dto.ReminderDto
import com.vero.tasky.agenda.data.util.LocalDateTimeConverter
import com.vero.tasky.agenda.domain.model.AgendaItem

object ReminderMapper {

    fun toReminder(reminderEntity: ReminderEntity) : AgendaItem.Reminder {
        return AgendaItem.Reminder(
            id = reminderEntity.id,
            title = reminderEntity.title,
            description = reminderEntity.description,
            remindAt = LocalDateTimeConverter.longToLocalDateTime(reminderEntity.remindAt),
            time = LocalDateTimeConverter.longToLocalDateTime(reminderEntity.time)
        )
    }

    fun toReminderEntity(reminderDto: ReminderDto) : ReminderEntity {
        return ReminderEntity(
            id = reminderDto.id,
            title = reminderDto.title,
            description = reminderDto.description,
            remindAt = reminderDto.remindAt,
            time = reminderDto.time
        )
    }

}