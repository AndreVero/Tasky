package com.vero.tasky.agenda.data.mappers

import com.vero.tasky.agenda.data.local.entities.LocalPhotoEntity
import com.vero.tasky.agenda.data.local.entities.RemotePhotoEntity
import com.vero.tasky.agenda.data.remote.network.dto.PhotoDto
import com.vero.tasky.agenda.domain.model.AgendaPhoto

object PhotoMapper {

    fun toPhotoEntity(eventId: String, photoDto: PhotoDto) : RemotePhotoEntity {
        return RemotePhotoEntity(
            key = photoDto.key,
            path = photoDto.url,
            eventId = eventId
        )
    }

    fun toNetworkPhoto(remotePhotoEntity: RemotePhotoEntity) : AgendaPhoto.RemotePhoto {
        return AgendaPhoto.RemotePhoto(
            key = remotePhotoEntity.key,
            url = remotePhotoEntity.path
        )
    }

    fun toLocalPhoto(localPhotoEntity: LocalPhotoEntity) : AgendaPhoto.LocalPhoto {
        return AgendaPhoto.LocalPhoto(
            uri = localPhotoEntity.path
        )
    }
}