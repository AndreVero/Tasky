package com.vero.tasky.agenda.domain.usecase.event

import com.vero.tasky.agenda.domain.repository.EventRepository

class FetchEventUseCase(
    private val eventRepository: EventRepository
) {

    suspend operator fun invoke(id: String) {
        eventRepository.fetchEvent(eventId = id)
    }
}