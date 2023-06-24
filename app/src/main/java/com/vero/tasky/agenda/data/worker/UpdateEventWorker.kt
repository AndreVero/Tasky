package com.vero.tasky.agenda.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.squareup.moshi.Moshi
import com.vero.tasky.agenda.data.local.dao.EventDao
import com.vero.tasky.agenda.data.local.dao.ModifiedAgendaItemDao
import com.vero.tasky.agenda.data.mappers.toLocalPhoto
import com.vero.tasky.agenda.data.mappers.toUpdateEventRequest
import com.vero.tasky.agenda.data.remote.network.api.EventApi
import com.vero.tasky.agenda.data.remote.network.request.UpdateEventRequest
import com.vero.tasky.agenda.data.util.multipart.MultipartParser
import com.vero.tasky.agenda.domain.workmanagerrunner.UpdateEventWorkerRunner
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
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
        if (runAttemptCount >= 3)
            return Result.failure()
        val modifiedEvents = modifiedAgendaItemDao.loadUpdatedEvents()
        coroutineScope {
            modifiedEvents.map { modifiedItem ->
                launch {
                    val agendaItem = eventDao.loadEvent(modifiedItem.id)
                    val isGoing = inputData.getBoolean(UpdateEventWorkerRunner.IS_GOING, true)
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
                    modifiedAgendaItemDao.deleteAgendaItem(modifiedItem)
                }
            }.joinAll()
        }
        return Result.success()
    }
}
