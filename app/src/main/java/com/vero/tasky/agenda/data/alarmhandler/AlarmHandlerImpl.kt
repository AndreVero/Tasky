package com.vero.tasky.agenda.data.alarmhandler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.vero.tasky.agenda.data.broadcastreceiver.AlarmReceiver
import com.vero.tasky.agenda.data.local.dao.EventDao
import com.vero.tasky.agenda.data.local.dao.ReminderDao
import com.vero.tasky.agenda.data.local.dao.TaskDao
import com.vero.tasky.agenda.data.mappers.toEvent
import com.vero.tasky.agenda.data.mappers.toReminder
import com.vero.tasky.agenda.data.mappers.toTask
import com.vero.tasky.agenda.data.util.LocalDateTimeConverter
import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.model.AgendaItemType
import com.vero.tasky.agenda.domain.remindermanager.AlarmData
import com.vero.tasky.agenda.domain.remindermanager.AlarmHandler
import com.vero.tasky.core.domain.local.UserPreferences
import java.time.LocalDateTime

class AlarmHandlerImpl(
    private val context: Context,
    private val eventDao: EventDao,
    private val taskDao: TaskDao,
    private val reminderDao: ReminderDao,
    private val preferences: UserPreferences
) : AlarmHandler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun setAlarm(data: AlarmData) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(AlarmHandler.ITEM_ID, data.itemId)
            putExtra(AlarmHandler.DESCRIPTION, data.description)
            putExtra(AlarmHandler.TITLE, data.title)
            putExtra(AlarmHandler.TYPE, data.type)
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            LocalDateTimeConverter.localDateTimeToLong(data.time) * 1000,
            PendingIntent.getBroadcast(
                context,
                data.itemId.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancelAlarm(id: String) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                id.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override suspend fun setAlarmForFutureAgendaItems() {
        val userId = preferences.getUser()?.userId ?: return

        val events = eventDao.loadAllEvents().map { it.toEvent() }
        val tasks = taskDao.loadAllTasks().map { it.toTask() }
        val reminders = reminderDao.loadAllReminders().map { it.toReminder() }
        (tasks + reminders).forEach {
            if (it.remindAt.isAfter(LocalDateTime.now())) {
                val type = if (it is AgendaItem.Task)
                    AgendaItemType.TASK
                else AgendaItemType.REMINDER

                setAlarm(
                    AlarmData(
                        time = it.remindAt,
                        itemId = it.id,
                        title = it.title,
                        description = it.description,
                        type = type.toString()
                    )
                )
            }
        }
        events.forEach { agendaItem ->
            if (agendaItem.remindAt.isAfter(LocalDateTime.now())) {
                val user = agendaItem.attendees
                    .find { attendee -> attendee.userId == userId }
                if (user?.isGoing == true) {
                    setAlarm(
                        AlarmData(
                            time = agendaItem.remindAt,
                            itemId = agendaItem.id,
                            title = agendaItem.title,
                            description = agendaItem.description,
                            type = AgendaItemType.EVENT.toString()
                        )
                    )
                }
            }
        }
    }

    override suspend fun cancelAllAlarms() {
        val events = eventDao.loadAllEvents().map { it.toEvent() }
        val tasks = taskDao.loadAllTasks().map { it.toTask() }
        val reminders = reminderDao.loadAllReminders().map { it.toReminder() }

        (events + tasks + reminders).forEach { cancelAlarm(it.id) }
    }
}