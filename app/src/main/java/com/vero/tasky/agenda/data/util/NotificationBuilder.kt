package com.vero.tasky.agenda.data.util

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.vero.tasky.MainActivity
import com.vero.tasky.R
import com.vero.tasky.agenda.domain.remindermanager.ReminderManager

object NotificationBuilder {

    fun buildReminderNotification(
        context: Context,
        title: String,
        description: String?,
        itemId: String
    ) : Notification {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(ReminderManager.ITEM_ID, itemId)
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(context, ReminderManager.CHANNEL_ID)
            .setSmallIcon(R.drawable.tasky_logo)
            .setContentTitle(title)
            .setContentText(description)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }
}