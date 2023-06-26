package com.vero.tasky.agenda.data.broadcastreceiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.vero.tasky.agenda.data.util.NotificationBuilder
import com.vero.tasky.agenda.domain.remindermanager.AlarmHandler

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null)
            return
        val itemId = intent.getStringExtra(AlarmHandler.ITEM_ID) ?: return
        val description = intent.getStringExtra(AlarmHandler.DESCRIPTION)
        val title = intent.getStringExtra(AlarmHandler.TITLE) ?: return

        val notification = NotificationBuilder.buildAlarmNotification(
            context = context,
            title = title,
            description = description,
            itemId = itemId
        )

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(itemId.hashCode(), notification)
    }
}