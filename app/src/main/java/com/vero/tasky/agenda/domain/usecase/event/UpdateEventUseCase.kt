package com.vero.tasky.agenda.domain.usecase.event

import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.model.AgendaItemUploadResult
import com.vero.tasky.agenda.domain.repository.EventRepository

class UpdateEventUseCase(
    private val eventRepository: EventRepository
) {

    suspend operator fun invoke(
        event: AgendaItem.Event,
        deletedPhotoKeys: List<String>,
        isGoing: Boolean,
    ) : Result<AgendaItemUploadResult> {
        return eventRepository.updateEvent(
            event = event,
            deletedPhotoKeys = deletedPhotoKeys,
            isGoing = isGoing
        )
    }
}