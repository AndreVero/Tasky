package com.vero.tasky.agenda.data.mappers

import com.vero.tasky.agenda.data.local.entities.ReminderEntity
import com.vero.tasky.agenda.data.remote.network.dto.ReminderDto
import com.vero.tasky.agenda.data.remote.network.request.ReminderRequest
import com.vero.tasky.agenda.data.util.LocalDateTimeConverter
import com.vero.tasky.agenda.data.util.LocalDateTimeConverter.localDateTimeToLong
import com.vero.tasky.agenda.domain.model.AgendaItem


fun ReminderEntity.toReminder(): AgendaItem.Reminder {
    return AgendaItem.Reminder(
        id = this.id,
        title = this.title,
        description = this.description,
        remindAt = LocalDateTimeConverter.longToLocalDateTime(this.remindAt),
        time = LocalDateTimeConverter.longToLocalDateTime(this.time)
    )
}

fun ReminderDto.toReminderEntity(): ReminderEntity {
    return ReminderEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        remindAt = this.remindAt,
        time = this.time
    )
}

fun AgendaItem.Reminder.toReminderEntity(): ReminderEntity {
    return ReminderEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        remindAt = localDateTimeToLong(this.remindAt),
        time = localDateTimeToLong(this.time)
    )
}

fun AgendaItem.Reminder.toReminderRequest(): ReminderRequest {
    return ReminderRequest(
        id = this.id,
        title = this.title,
        description = this.description,
        remindAt = localDateTimeToLong(this.remindAt),
        time = localDateTimeToLong(this.time),
    )
}

