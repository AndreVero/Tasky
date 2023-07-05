package com.vero.tasky.agenda.data.broadcastreceiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.vero.tasky.agenda.data.util.NotificationBuilder
import com.vero.tasky.agenda.domain.model.AgendaItemType
import com.vero.tasky.agenda.domain.remindermanager.AlarmHandler
import com.vero.tasky.core.presentation.navigation.NavigationConstants.Companion.DEEP_LINK_HOST
import com.vero.tasky.core.presentation.navigation.NavigationConstants.Companion.EVENT_ITEM_ID
import com.vero.tasky.core.presentation.navigation.NavigationConstants.Companion.ITEM_ID
import com.vero.tasky.core.presentation.navigation.NavigationConstants.Companion.REMINDER_ITEM_ID
import com.vero.tasky.core.presentation.navigation.NavigationConstants.Companion.TASK_ITEM_ID

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null)
            return
        val itemId = intent.getStringExtra(AlarmHandler.ITEM_ID) ?: return
        val typeStr = intent.getStringExtra(AlarmHandler.TYPE) ?: return
        val description = intent.getStringExtra(AlarmHandler.DESCRIPTION)
        val title = intent.getStringExtra(AlarmHandler.TITLE) ?: return
        val type = when (AgendaItemType.valueOf(typeStr)) {
            AgendaItemType.EVENT -> "$EVENT_ITEM_ID=$ITEM_ID"
            AgendaItemType.TASK -> "$TASK_ITEM_ID=$ITEM_ID"
            AgendaItemType.REMINDER -> "$REMINDER_ITEM_ID=$ITEM_ID"
        }

        val notification = NotificationBuilder.buildAlarmNotification(
            context = context,
            title = title,
            description = description,
            uriStr = "$DEEP_LINK_HOST?$type"
        )

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(itemId.hashCode(), notification)
    }
}