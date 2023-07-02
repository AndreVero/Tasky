package com.vero.tasky.agenda.domain.usecase.event

import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.model.AgendaItemUploadResult
import com.vero.tasky.agenda.domain.model.Attendee
import com.vero.tasky.agenda.domain.model.ModificationType
import com.vero.tasky.agenda.domain.repository.EventRepository

class SaveEventUseCase(
    private val eventRepository: EventRepository
) {

    suspend operator fun invoke(
        event: AgendaItem.Event,
        isGoing: Boolean,
        deletedPhotoKeys: List<String>,
        modificationType: ModificationType,
        currentUser: Attendee? = null,
    ) : Result<AgendaItemUploadResult> {
        return eventRepository.saveEvent(
            event = if (currentUser != null)
                event.copy(attendees = event.attendees + currentUser)
            else event,
            isGoing = isGoing,
            deletedPhotoKeys = deletedPhotoKeys,
            modificationType = modificationType
        )
    }
}