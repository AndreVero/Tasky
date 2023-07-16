package com.vero.tasky.agenda.worker.factories

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.vero.tasky.agenda.data.local.dao.ModifiedAgendaItemDao
import com.vero.tasky.agenda.data.worker.SyncAgendaDeletedItemsWorker
import com.vero.tasky.agenda.domain.repository.AgendaRepository

class SyncAgendaDeletedItemsWorkerFactory(
    private val agendaRepository: AgendaRepository,
    private val modifiedAgendaItemDao: ModifiedAgendaItemDao
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context, workerClassName: String,
        workerParameters: WorkerParameters
    ) : ListenableWorker {
        return SyncAgendaDeletedItemsWorker(
            context = appContext,
            workerParameters = workerParameters,
            agendaRepository = agendaRepository,
            modifiedAgendaItemDao = modifiedAgendaItemDao
        )
    }
}