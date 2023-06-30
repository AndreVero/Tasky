package com.vero.tasky.agenda.domain.usecase.event

import com.vero.tasky.core.domain.usecase.ValidateEmailUseCase

data class EventUseCases(
    val checkAttendee: CheckAttendeeUseCase,
    val saveEvent: SaveEventUseCase,
    val deleteAttendee: DeleteAttendeeUseCase,
    val deleteEvent: DeleteEventUseCase,
    val fetchEvent: FetchEventUseCase,
    val getEvent: GetEventInfoUseCase,
    val validateEmailUseCase: ValidateEmailUseCase,
)