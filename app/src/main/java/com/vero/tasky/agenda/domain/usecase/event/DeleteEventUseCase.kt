package com.vero.tasky.agenda.domain.usecase.event

import com.vero.tasky.agenda.domain.repository.EventRepository

class DeleteEventUseCase(
    private val eventRepository: EventRepository
) {

    operator fun invoke(id: String) = eventRepository.getEvent(id)
}