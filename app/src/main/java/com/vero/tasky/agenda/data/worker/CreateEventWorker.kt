package com.vero.tasky.agenda.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.squareup.moshi.Moshi
import com.vero.tasky.agenda.data.local.dao.EventDao
import com.vero.tasky.agenda.data.local.dao.ModifiedAgendaItemDao
import com.vero.tasky.agenda.data.local.entities.ModifiedAgendaItemEntity
import com.vero.tasky.agenda.data.mappers.toCreateEventRequest
import com.vero.tasky.agenda.data.mappers.toLocalPhoto
import com.vero.tasky.agenda.data.remote.network.api.EventApi
import com.vero.tasky.agenda.data.remote.network.request.CreateEventRequest
import com.vero.tasky.agenda.data.util.multipart.MultipartParser
import com.vero.tasky.agenda.domain.model.AgendaItemType
import com.vero.tasky.agenda.domain.model.ModificationType
import com.vero.tasky.agenda.domain.workmanagerrunner.UpdateEventWorkerRunner
import com.vero.tasky.core.data.remote.safeSuspendCall
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import okhttp3.MultipartBody

@HiltWorker
class CreateEventWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val multipartParser: MultipartParser,
    private val modifiedAgendaItemDao: ModifiedAgendaItemDao,
    private val eventDao: EventDao,
    private val api: EventApi,
    private val moshi: Moshi,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val eventId = inputData.getString(UpdateEventWorkerRunner.EVENT_ID) ?: ""

        if (runAttemptCount >= 3) {
            modifiedAgendaItemDao.insertAgendaItem(
                ModifiedAgendaItemEntity(
                    id = eventId,
                    modificationType = ModificationType.UPDATED,
                    agendaItemType = AgendaItemType.EVENT
                )
            )
            return Result.failure()
        }
        val result = safeSuspendCall {
            val agendaItem = eventDao.loadEvent(eventId)

            val adapter = moshi.adapter(CreateEventRequest::class.java)
            val createEventRequest = adapter.toJson(
                agendaItem.event.toCreateEventRequest(agendaItem.attendees)
            )
            val multipartPhotos = multipartParser.getMultipartPhotos(
                agendaItem.localPhotos.map { it.toLocalPhoto() }
            )
            api.createEvent(
                createEventRequest = MultipartBody.Part.createFormData(
                    "create_event_request",
                    createEventRequest
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