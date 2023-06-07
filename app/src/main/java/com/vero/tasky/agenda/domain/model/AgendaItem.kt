package com.vero.tasky.agenda.domain.model

import java.time.LocalDateTime

sealed class AgendaItem(
    open val id: String,
    open val title: String,
    open val description: String?,
    open val time: LocalDateTime,
    open val remindAt: LocalDateTime
) {

    data class Reminder(
        override val id: String,
        override val title: String,
        override val description: String?,
        override val remindAt: LocalDateTime,
        override val time: LocalDateTime,
    )  : AgendaItem(
            id = id,
            title = title,
            description = description,
            remindAt = remindAt,
            time = time
        )

    data class Task(
        override val id: String,
        override val title: String,
        override val description: String?,
        override val remindAt: LocalDateTime,
        override val time: LocalDateTime,
        val isDone: Boolean
    ) : AgendaItem(
            id = id,
            title = title,
            description = description,
            remindAt = remindAt,
            time = time
        )

    data class Event(
        override val id: String,
        override val title: String,
        override val description: String?,
        override val remindAt: LocalDateTime,
        override val time: LocalDateTime,
        val to: LocalDateTime,
        val host: String,
        val isUserEventCreator: Boolean,
        val attendees: List<Attendee>,
        val photos: List<AgendaPhoto>
    ) : AgendaItem(
            id = id,
            title = title,
            description = description,
            remindAt = remindAt,
            time = time
        )
}