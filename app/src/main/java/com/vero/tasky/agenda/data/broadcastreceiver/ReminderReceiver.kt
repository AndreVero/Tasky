package com.vero.tasky.agenda.data.broadcastreceiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.vero.tasky.agenda.data.util.NotificationBuilder
import com.vero.tasky.agenda.domain.remindermanager.ReminderManager

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val itemId = intent?.getStringExtra(ReminderManager.ITEM_ID) ?: return
            val description = intent.getStringExtra(ReminderManager.DESCRIPTION)
            val title = intent.getStringExtra(ReminderManager.TITLE) ?: return

            val notification = NotificationBuilder.buildReminderNotification(
                context = it,
                title = title,
                description = description,
                itemId = itemId
            )

            val notificationManager: NotificationManager =
                it.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(1, notification)
        }
    }
}