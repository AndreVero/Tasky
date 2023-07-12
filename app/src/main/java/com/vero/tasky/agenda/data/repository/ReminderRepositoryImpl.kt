package com.vero.tasky.agenda.data.repository

import com.vero.tasky.agenda.data.local.dao.ModifiedAgendaItemDao
import com.vero.tasky.agenda.data.local.dao.ReminderDao
import com.vero.tasky.agenda.data.local.entities.ModifiedAgendaItemEntity
import com.vero.tasky.agenda.data.mappers.*
import com.vero.tasky.agenda.data.remote.network.api.ReminderApi
import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.model.AgendaItemType
import com.vero.tasky.agenda.domain.model.ModificationType
import com.vero.tasky.agenda.domain.remindermanager.AlarmData
import com.vero.tasky.agenda.domain.remindermanager.AlarmHandler
import com.vero.tasky.agenda.domain.repository.ReminderRepository
import com.vero.tasky.core.data.remote.safeSuspendCall

class ReminderRepositoryImpl(
    private val api: ReminderApi,
    private val reminderDao: ReminderDao,
    private val modifiedAgendaItemDao: ModifiedAgendaItemDao,
    private val alarmHandler: AlarmHandler,
) : ReminderRepository {

    override suspend fun saveReminder(
        reminder: AgendaItem.Reminder,
        modificationType: ModificationType
    ) {
        alarmHandler.setAlarm(
            AlarmData(
                time = reminder.remindAt,
                itemId = reminder.id,
                title = reminder.title,
                description = reminder.description,
                type = AgendaItemType.REMINDER.toString()
            )
        )
        reminderDao.insertReminders(reminder.toReminderEntity())
        safeSuspendCall {
            if (modificationType == ModificationType.CREATED)
                api.createReminder(reminder.toReminderRequest())
            else
                api.updateReminder(reminder.toReminderRequest())
        }.onFailure {
                modifiedAgendaItemDao.insertAgendaItem(
                    ModifiedAgendaItemEntity(
                        id = reminder.id,
                        agendaItemType = AgendaItemType.REMINDER,
                        modificationType = modificationType
                    )
                )
            }
    }

    override suspend fun getReminder(id: String): AgendaItem.Reminder {
        return reminderDao.loadReminder(id).toReminder()
    }

    override suspend fun deleteReminder(reminder: AgendaItem.Reminder) {
        alarmHandler.cancelAlarm(reminder.id)
        reminderDao.deleteReminders(reminder.toReminderEntity())

        safeSuspendCall {
            api.deleteReminder(reminderId = reminder.id)
        }.onFailure {
            modifiedAgendaItemDao.insertAgendaItem(
                ModifiedAgendaItemEntity(
                    id = reminder.id,
                    agendaItemType = AgendaItemType.REMINDER,
                    modificationType = ModificationType.DELETED
                )
            )
        }
    }
}