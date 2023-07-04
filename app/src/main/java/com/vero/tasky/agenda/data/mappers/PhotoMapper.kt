package com.vero.tasky.agenda.data.mappers

import com.vero.tasky.agenda.data.local.entities.LocalPhotoEntity
import com.vero.tasky.agenda.data.local.entities.RemotePhotoEntity
import com.vero.tasky.agenda.data.remote.network.dto.PhotoDto
import com.vero.tasky.agenda.domain.model.AgendaPhoto

fun PhotoDto.toPhotoEntity(eventId: String): RemotePhotoEntity {
    return RemotePhotoEntity(
        key = this.key,
        path = this.url,
        eventId = eventId
    )
}

fun RemotePhotoEntity.toRemotePhoto(): AgendaPhoto.RemotePhoto {
    return AgendaPhoto.RemotePhoto(
        key = this.key,
        url = this.path
    )
}

fun LocalPhotoEntity.toLocalPhoto(): AgendaPhoto.LocalPhoto {
    return AgendaPhoto.LocalPhoto(
        uri = this.path
    )
}

fun AgendaPhoto.LocalPhoto.toLocalPhotoEntity(eventId: String): LocalPhotoEntity {
    return LocalPhotoEntity(
        path = this.path,
        eventId = eventId
    )
}
