package com.vero.tasky.agenda.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.vero.tasky.agenda.data.local.AgendaDatabase
import com.vero.tasky.agenda.data.mappers.toTaskEntity
import com.vero.tasky.agenda.data.util.LocalDateTimeConverter
import com.vero.tasky.agenda.domain.model.AgendaItem
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
@SmallTest
class TaskDaoTest {

    private lateinit var database: AgendaDatabase
    private lateinit var taskDao: TaskDao

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dateTimeStartOfDay = LocalDate.of(2023,1,1).atStartOfDay()
    private val dateTimeEndOfDay = dateTimeStartOfDay.withHour(23).withMinute(59)
    private val task = AgendaItem.Task(
        id = "1",
        title = "",
        description = "",
        remindAt = dateTimeStartOfDay,
        time = dateTimeStartOfDay,
        isDone = false
    )

    @Before
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AgendaDatabase::class.java
        ).allowMainThreadQueries().build()

        taskDao = database.taskDao()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun getEventsForDay_returnSuccessfully() = runTest {
        taskDao.insertTasks(
            task.toTaskEntity(),
            task.copy(id = "2", time = dateTimeEndOfDay).toTaskEntity(),
            task.copy(id = "3", time = dateTimeStartOfDay.plusDays(1)).toTaskEntity(),
        )
        taskDao.loadTasksForDay(
            from = LocalDateTimeConverter.getEpochForUTC(dateTimeStartOfDay),
            to = LocalDateTimeConverter.getEpochForUTC(dateTimeEndOfDay)
        ).test {
            val items = awaitItem()
            assertThat(items.size).isEqualTo(2)
        }
    }
}