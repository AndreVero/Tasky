package com.vero.tasky.agenda.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.squareup.moshi.Moshi
import com.vero.tasky.agenda.data.local.dao.EventDao
import com.vero.tasky.agenda.data.local.dao.ModifiedAgendaItemDao
import com.vero.tasky.agenda.data.mappers.toCreateEventRequest
import com.vero.tasky.agenda.data.mappers.toLocalPhoto
import com.vero.tasky.agenda.data.remote.network.api.EventApi
import com.vero.tasky.agenda.data.remote.network.request.CreateEventRequest
import com.vero.tasky.agenda.data.util.multipart.MultipartParser
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
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
        if (runAttemptCount >= 3)
            return Result.failure()
        val modifiedEvents = modifiedAgendaItemDao.loadCreatedEvents()
        coroutineScope {
            modifiedEvents.map { modifiedItem ->
                launch {
                    val agendaItem = eventDao.loadEvent(modifiedItem.id)

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
                    modifiedAgendaItemDao.deleteAgendaItem(modifiedItem)
                }
            }.joinAll()
        }
        return Result.success()
    }
}