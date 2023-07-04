package com.vero.tasky.agenda.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.squareup.moshi.Moshi
import com.vero.tasky.agenda.data.local.EventWithPhotosAndAttendees
import com.vero.tasky.agenda.data.local.dao.EventDao
import com.vero.tasky.agenda.data.local.dao.ModifiedAgendaItemDao
import com.vero.tasky.agenda.data.local.entities.ModifiedAgendaItemEntity
import com.vero.tasky.agenda.data.mappers.toCreateEventRequest
import com.vero.tasky.agenda.data.mappers.toLocalPhoto
import com.vero.tasky.agenda.data.mappers.toPhotoEntity
import com.vero.tasky.agenda.data.mappers.toUpdateEventRequest
import com.vero.tasky.agenda.data.remote.network.api.EventApi
import com.vero.tasky.agenda.data.remote.network.dto.EventDto
import com.vero.tasky.agenda.data.remote.network.request.CreateEventRequest
import com.vero.tasky.agenda.data.remote.network.request.UpdateEventRequest
import com.vero.tasky.agenda.data.util.multipart.MultipartParser
import com.vero.tasky.agenda.domain.model.AgendaItemType
import com.vero.tasky.agenda.domain.model.ModificationType
import com.vero.tasky.agenda.domain.workmanagerrunner.SaveEventWorkerRunner
import com.vero.tasky.core.data.remote.safeSuspendCall
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import okhttp3.MultipartBody
import java.io.File

@HiltWorker
class SaveEventWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val multipartParser: MultipartParser,
    private val modifiedAgendaItemDao: ModifiedAgendaItemDao,
    private val eventDao: EventDao,
    private val api: EventApi,
    private val moshi: Moshi,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val isGoing = inputData.getBoolean(SaveEventWorkerRunner.IS_GOING, true)
        val eventId = inputData.getString(SaveEventWorkerRunner.EVENT_ID)
        val modificationTypeStr = inputData.getString(SaveEventWorkerRunner.MODIFICATION_TYPE)

        if (eventId == null || modificationTypeStr == null)
            return Result.success()

        val modificationType = ModificationType.valueOf(modificationTypeStr)

        if (runAttemptCount >= 3) {
            modifiedAgendaItemDao.insertAgendaItem(
                ModifiedAgendaItemEntity(
                    id = eventId,
                    modificationType = modificationType,
                    agendaItemType = AgendaItemType.EVENT
                )
            )
            return Result.failure()
        }

        var multipartPhotoFiles : List<File>? = null

        val result = safeSuspendCall {
            val agendaItem = eventDao.loadEvent(eventId)

            val multipartPhotos = multipartParser.getMultipartPhotos(
                agendaItem.localPhotos.map { it.toLocalPhoto() }
            )
            multipartPhotoFiles = multipartPhotos.map { it.first }

            when(modificationType) {
                ModificationType.CREATED -> createEvent(agendaItem, multipartPhotos.map {it.second })
                ModificationType.UPDATED -> updateEvent(agendaItem, multipartPhotos.map { it.second }, isGoing)
                else -> return@safeSuspendCall
            }
        }
        multipartPhotoFiles?.forEach { it.delete() }
        return if (result.isSuccess)
            Result.success()
        else
            Result.retry()
    }

    private suspend fun updateEvent(
        agendaItem: EventWithPhotosAndAttendees,
        multipartPhotos: List<MultipartBody.Part>,
        isGoing: Boolean
    ) {
        val adapter = moshi.adapter(UpdateEventRequest::class.java)
        val updateEventRequest = adapter.toJson(
            agendaItem.event.toUpdateEventRequest(
                attendeesEntity = agendaItem.attendees,
                isGoing = isGoing,
                deletedPhotoKeys = agendaItem.deletedPhoto.map { it.key }
            )
        )
        val eventDto = api.updateEvent(
            updateEventRequest = MultipartBody.Part.createFormData(
                "update_event_request",
                updateEventRequest
            ),
            photos = multipartPhotos
        )
        updatePhotosEntities(eventDto, agendaItem)
    }

    private suspend fun createEvent(
        agendaItem: EventWithPhotosAndAttendees,
        multipartPhotos: List<MultipartBody.Part>
    ) {
        val adapter = moshi.adapter(CreateEventRequest::class.java)

        val createEventRequest = adapter.toJson(
            agendaItem.event.toCreateEventRequest(agendaItem.attendees)
        )
        val eventDto = api.createEvent(
            createEventRequest = MultipartBody.Part.createFormData(
                "create_event_request",
                createEventRequest
            ),
            photos = multipartPhotos
        )
        updatePhotosEntities(eventDto, agendaItem)
    }

    private suspend fun updatePhotosEntities(eventDto: EventDto, agendaItem: EventWithPhotosAndAttendees) {
        supervisorScope {
            val remotePhotoJobs = eventDto.photos.map {
                launch { eventDao.insertRemotePhotoEntity(it.toPhotoEntity(agendaItem.event.id)) }
            }
            val localPhotoJobs = agendaItem.localPhotos.map {
                launch { eventDao.deleteLocalPhotos(it) }
            }
            (remotePhotoJobs + localPhotoJobs).joinAll()
        }
    }
}