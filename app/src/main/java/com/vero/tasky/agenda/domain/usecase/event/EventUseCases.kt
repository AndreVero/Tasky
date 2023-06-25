package com.vero.tasky.agenda.domain.usecase.event

data class EventUseCases(
    val checkAttendee: CheckAttendeeUseCase,
    val createEvent: CreateEventUseCase,
    val deleteAttendee: DeleteAttendeeUseCase,
    val deleteEvent: DeleteEventUseCase,
    val fetchEvent: FetchEventUseCase,
    val getEvent: GetEventInfoUseCase,
    val updateEvent: UpdateEventUseCase,
)