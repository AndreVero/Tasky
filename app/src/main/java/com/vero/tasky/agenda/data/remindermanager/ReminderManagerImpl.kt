package com.vero.tasky.agenda.data.remindermanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.vero.tasky.agenda.data.broadcastreceiver.ReminderReceiver
import com.vero.tasky.agenda.data.util.LocalDateTimeConverter
import com.vero.tasky.agenda.domain.remindermanager.ReminderData
import com.vero.tasky.agenda.domain.remindermanager.ReminderManager

class ReminderManagerImpl(
    private val context : Context
) : ReminderManager {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun setReminder(data: ReminderData) {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(ReminderManager.ITEM_ID, data.itemId)
            putExtra(ReminderManager.DESCRIPTION, data.description)
            putExtra(ReminderManager.TITLE, data.title)
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

    override fun cancelReminder(id: String) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                id.hashCode(),
                Intent(context, ReminderReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}