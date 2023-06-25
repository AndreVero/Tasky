package com.vero.tasky.agenda.domain.usecase.event

import com.vero.tasky.agenda.domain.repository.EventRepository

class DeleteAttendeeUseCase(
    private val eventRepository: EventRepository
) {

    suspend operator fun invoke(eventId: String) : Result<Unit> {
        return eventRepository.deleteAttendee(eventId)
    }
}