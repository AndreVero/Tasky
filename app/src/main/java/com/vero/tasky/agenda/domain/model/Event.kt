package com.vero.tasky.agenda.domain.model

data class Event(
    override val id: String,
    override val title: String,
    override val description: String?,
    override val remindAt: Long,
    override val time: Long,
    val to: Long,
    val host: String,
    val isUserEventCreator: Boolean,
    val attendees: List<Attendee>,
    val networkPhotos: List<NetworkPhoto>
) : AgendaItem