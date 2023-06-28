package com.vero.tasky.agenda.data.remindermanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.vero.tasky.agenda.data.broadcastreceiver.AlarmReceiver
import com.vero.tasky.agenda.data.util.LocalDateTimeConverter
import com.vero.tasky.agenda.domain.remindermanager.AlarmData
import com.vero.tasky.agenda.domain.remindermanager.AlarmHandler

class AlarmHandlerImpl(
    private val context : Context
) : AlarmHandler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun setAlarm(data: AlarmData) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(AlarmHandler.ITEM_ID, data.itemId)
            putExtra(AlarmHandler.DESCRIPTION, data.description)
            putExtra(AlarmHandler.TITLE, data.title)
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
}