package com.vero.tasky.agenda.data.util

import android.app.Notification
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.vero.tasky.MainActivity
import com.vero.tasky.R
import com.vero.tasky.agenda.domain.remindermanager.AlarmHandler

object NotificationBuilder {

    fun buildAlarmNotification(
        context: Context,
        title: String,
        description: String?,
        uriStr: String
    ) : Notification {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(uriStr),
            context,
            MainActivity::class.java
        )
        val pendingIntent: PendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(
                0,
                PendingIntent.FLAG_IMMUTABLE
            )
        }

        return NotificationCompat.Builder(context, AlarmHandler.CHANNEL_ID)
            .setSmallIcon(R.drawable.tasky_logo)
            .setContentTitle(title)
            .setContentText(description)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }
}