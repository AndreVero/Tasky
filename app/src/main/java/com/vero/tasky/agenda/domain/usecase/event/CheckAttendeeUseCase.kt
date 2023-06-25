package com.vero.tasky.agenda.domain.usecase.event

import com.vero.tasky.agenda.domain.model.AttendeeBaseInfo
import com.vero.tasky.agenda.domain.repository.EventRepository

class CheckAttendeeUseCase(
    private val eventRepository: EventRepository
) {

    suspend operator fun invoke(email: String) : Result<AttendeeBaseInfo?> {
        return eventRepository.getAttendee(email)
    }
}