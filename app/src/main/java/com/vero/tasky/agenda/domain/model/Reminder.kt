package com.vero.tasky.agenda.domain.model

data class Reminder(
    override val id: String,
    override val title: String,
    override val description: String?,
    override val remindAt: Long,
    override val time: Long,
) : AgendaItem
