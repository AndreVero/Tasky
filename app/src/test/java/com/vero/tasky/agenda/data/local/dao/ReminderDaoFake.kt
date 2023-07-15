package com.vero.tasky.agenda.data.local.dao

import com.vero.tasky.agenda.data.local.entities.ReminderEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ReminderDaoFake : ReminderDao {

    private val reminderList = mutableSetOf<ReminderEntity>()

    override suspend fun insertReminders(vararg reminders: ReminderEntity) {
        reminderList.addAll(reminders)
    }

    override suspend fun updateReminders(vararg reminders: ReminderEntity) {}

    override suspend fun deleteReminders(vararg reminders: ReminderEntity) {}

    override fun loadRemindersForDay(from: Long, to: Long): Flow<List<ReminderEntity>> {
        return  flowOf(reminderList.toList())
    }

    override suspend fun loadReminder(id: String): ReminderEntity {
        return reminderList.find { it.id == id }!!
    }

    override suspend fun loadAllReminders(): List<ReminderEntity> {
        return reminderList.toList()
    }
}