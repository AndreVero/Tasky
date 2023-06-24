package com.vero.tasky.agenda.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.squareup.moshi.Moshi
import com.vero.tasky.agenda.data.local.dao.EventDao
import com.vero.tasky.agenda.data.local.dao.ModifiedAgendaItemDao
import com.vero.tasky.agenda.data.local.entities.ModifiedAgendaItemEntity
import com.vero.tasky.agenda.data.mappers.toLocalPhoto
import com.vero.tasky.agenda.data.mappers.toUpdateEventRequest
import com.vero.tasky.agenda.data.remote.network.api.EventApi
import com.vero.tasky.agenda.data.remote.network.request.UpdateEventRequest
import com.vero.tasky.agenda.data.util.multipart.MultipartParser
import com.vero.tasky.agenda.domain.model.AgendaItemType
import com.vero.tasky.agenda.domain.model.ModificationType
import com.vero.tasky.agenda.domain.workmanagerrunner.UpdateEventWorkerRunner
import com.vero.tasky.core.data.remote.safeSuspendCall
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import okhttp3.MultipartBody

@HiltWorker
class UpdateEventWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val multipartParser: MultipartParser,
    private val modifiedAgendaItemDao: ModifiedAgendaItemDao,
    private val eventDao: EventDao,
    private val api: EventApi,
    private val moshi: Moshi
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val isGoing = inputData.getBoolean(UpdateEventWorkerRunner.IS_GOING, true)
        val eventId = inputData.getString(UpdateEventWorkerRunner.EVENT_ID) ?: ""

        if (runAttemptCount >= 3) {
            modifiedAgendaItemDao.insertAgendaItem(
                ModifiedAgendaItemEntity(
                    id = eventId,
                    modificationType = ModificationType.CREATED,
                    agendaItemType = AgendaItemType.EVENT
                )
            )
            return Result.failure()
        }

        val result = safeSuspendCall {
            val agendaItem = eventDao.loadEvent(eventId)

            val adapter = moshi.adapter(UpdateEventRequest::class.java)
            val updateEventRequest = adapter.toJson(
                agendaItem.event.toUpdateEventRequest(
                    attendeesEntity = agendaItem.attendees,
                    isGoing = isGoing,
                    deletedPhotoKeys = agendaItem.deletedPhoto.map { it.key }
                )
            )
            val multipartPhotos = multipartParser.getMultipartPhotos(
                agendaItem.localPhotos.map { it.toLocalPhoto() }
            )
            api.updateEvent(
                updateEventRequest = MultipartBody.Part.createFormData(
                    "update_event_request",
                    updateEventRequest
                ),
                photos = multipartPhotos
            )
        }
        return if (result.isSuccess)
            Result.success()
        else
            Result.retry()
    }
}
