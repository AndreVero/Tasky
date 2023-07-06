package com.vero.tasky.agenda.data.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.vero.tasky.agenda.domain.remindermanager.AlarmHandler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BootCompletedReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmHandler: AlarmHandler

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            goAsync { alarmHandler.setAlarmForFutureAgendaItems() }
        }
    }
}