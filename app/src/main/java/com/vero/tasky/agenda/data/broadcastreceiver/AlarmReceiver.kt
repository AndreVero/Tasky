package com.vero.tasky.agenda.data.broadcastreceiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.vero.tasky.agenda.data.util.NotificationBuilder
import com.vero.tasky.agenda.domain.model.AgendaItemType
import com.vero.tasky.agenda.domain.remindermanager.AlarmHandler
import com.vero.tasky.core.presentation.navigation.NavigationConstants.Companion.getEventDeepLink
import com.vero.tasky.core.presentation.navigation.NavigationConstants.Companion.getReminderDeepLink
import com.vero.tasky.core.presentation.navigation.NavigationConstants.Companion.getTaskDeepLink

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null)
            return
        val itemId = intent.getStringExtra(AlarmHandler.ITEM_ID) ?: return
        val typeStr = intent.getStringExtra(AlarmHandler.TYPE) ?: return
        val description = intent.getStringExtra(AlarmHandler.DESCRIPTION)
        val title = intent.getStringExtra(AlarmHandler.TITLE) ?: return
        val type = when (AgendaItemType.valueOf(typeStr)) {
            AgendaItemType.EVENT -> getEventDeepLink(itemId)
            AgendaItemType.TASK -> getTaskDeepLink(itemId)
            AgendaItemType.REMINDER -> getReminderDeepLink(itemId)
        }

        val notification = NotificationBuilder.buildAlarmNotification(
            context = context,
            title = title,
            description = description,
            uriStr = type
        )

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(itemId.hashCode(), notification)
    }
}