package com.vero.tasky.agenda.domain.usecase.event

import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.repository.EventRepository

class DeleteEventUseCase(
    private val eventRepository: EventRepository
) {

    suspend operator fun invoke(event: AgendaItem.Event) = eventRepository.deleteEvent(event)
}