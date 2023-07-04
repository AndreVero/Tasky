package com.vero.tasky.agenda.data.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.vero.tasky.agenda.data.local.dao.EventDao
import com.vero.tasky.agenda.data.local.dao.ReminderDao
import com.vero.tasky.agenda.data.local.dao.TaskDao
import com.vero.tasky.agenda.data.mappers.toEvent
import com.vero.tasky.agenda.data.mappers.toReminder
import com.vero.tasky.agenda.data.mappers.toTask
import com.vero.tasky.agenda.domain.remindermanager.AlarmData
import com.vero.tasky.agenda.domain.remindermanager.AlarmHandler
import com.vero.tasky.core.domain.local.UserPreferences
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class BootCompletedReceiver : BroadcastReceiver() {

    @Inject
    lateinit var eventDao: EventDao

    @Inject
    lateinit var taskDao: TaskDao

    @Inject
    lateinit var reminderDao: ReminderDao

    @Inject
    lateinit var alarmHandler: AlarmHandler

    @Inject
    lateinit var preferences: UserPreferences

    override fun onReceive(context: Context?, intent: Intent?) {
        val userId = preferences.getUser()?.userId ?: return

        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            val events = eventDao.loadAllEvents().map { it.toEvent() }
            val tasks = taskDao.loadAllTasks().map { it.toTask() }
            val reminders = reminderDao.loadAllReminders().map { it.toReminder() }
            (tasks + reminders).forEach {
                if (it.time.isAfter(LocalDateTime.now())) {
                    alarmHandler.setAlarm(
                        AlarmData(
                            time = it.remindAt,
                            itemId = it.id,
                            title = it.title,
                            description = it.description
                        )
                    )
                }
            }
            events.forEach { agendaItem ->
                if (agendaItem.time.isAfter(LocalDateTime.now())) {
                    val user = agendaItem.attendees
                        .find { attendee -> attendee.userId == userId }
                    if (user != null && user.isGoing) {
                        alarmHandler.setAlarm(
                            AlarmData(
                                time = agendaItem.remindAt,
                                itemId = agendaItem.id,
                                title = agendaItem.title,
                                description = agendaItem.description
                            )
                        )
                    }
                }
            }
        }
    }
}