package com.vero.tasky.agenda.worker

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.work.testing.TestListenableWorkerBuilder
import com.google.common.truth.Truth.assertThat
import com.vero.tasky.agenda.data.local.dao.ModifiedAgendaItemDaoFake
import com.vero.tasky.agenda.data.local.entities.ModifiedAgendaItemEntity
import com.vero.tasky.agenda.data.repository.AgendaRepositoryFake
import com.vero.tasky.agenda.data.worker.SyncAgendaDeletedItemsWorker
import com.vero.tasky.agenda.domain.model.AgendaItemType
import com.vero.tasky.agenda.domain.model.ModificationType
import com.vero.tasky.agenda.worker.factories.SyncAgendaDeletedItemsWorkerFactory
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.HttpException
import retrofit2.Response

@RunWith(AndroidJUnit4::class)
@SmallTest
class SyncAgendaDeletedItemsWorkerTest {

    private lateinit var worker: SyncAgendaDeletedItemsWorker
    private lateinit var agendaRepository: AgendaRepositoryFake
    private lateinit var modifiedAgendaItemDao: ModifiedAgendaItemDaoFake

    private val modifiedAgendaItemEntity = ModifiedAgendaItemEntity(
        id = "1",
        agendaItemType = AgendaItemType.REMINDER,
        modificationType = ModificationType.DELETED
    )

    @Before
    fun setUp() {
        agendaRepository = AgendaRepositoryFake()
        modifiedAgendaItemDao = ModifiedAgendaItemDaoFake()
        worker = TestListenableWorkerBuilder<SyncAgendaDeletedItemsWorker>(
            ApplicationProvider.getApplicationContext()
        ).setWorkerFactory(
            SyncAgendaDeletedItemsWorkerFactory(
                agendaRepository = agendaRepository,
                modifiedAgendaItemDao = modifiedAgendaItemDao
            )
        ).build()
    }

    @Test
    fun syncAgenda_agendaItemWasDeleted() = runTest {
        modifiedAgendaItemDao.insertAgendaItem(modifiedAgendaItemEntity)
        val result = worker.doWork()
        assertThat(result).isInstanceOf(androidx.work.ListenableWorker.Result.Success::class.java)
        val items = modifiedAgendaItemDao.loadDeletedAgendaItems()
        assertThat(items).hasSize(0)
    }

    @Test
    fun syncAgenda_networkError_resultRetry() = runTest {
        modifiedAgendaItemDao.insertAgendaItem(modifiedAgendaItemEntity)
        agendaRepository.result = Result.failure(Throwable())
        val result = worker.doWork()
        assertThat(result).isInstanceOf(androidx.work.ListenableWorker.Result.Retry::class.java)
        val items = modifiedAgendaItemDao.loadDeletedAgendaItems()
        assertThat(items).hasSize(1)
    }

    @Test
    fun syncAgenda_networkErrorUnauthorized_resultFailure() = runTest {
        modifiedAgendaItemDao.insertAgendaItem(modifiedAgendaItemEntity)
        agendaRepository.result = Result.failure(HttpException(Response.error<String>(401, "".toResponseBody())))
        val result = worker.doWork()
        assertThat(result).isInstanceOf(androidx.work.ListenableWorker.Result.Failure::class.java)
        val items = modifiedAgendaItemDao.loadDeletedAgendaItems()
        assertThat(items).hasSize(1)
    }
}