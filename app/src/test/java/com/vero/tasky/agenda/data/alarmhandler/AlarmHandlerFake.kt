package com.vero.tasky.agenda.data.alarmhandler

import com.vero.tasky.agenda.domain.remindermanager.AlarmData
import com.vero.tasky.agenda.domain.remindermanager.AlarmHandler

class AlarmHandlerFake: AlarmHandler {
    override fun setAlarm(data: AlarmData) {}

    override fun cancelAlarm(id: String) {}

    override suspend fun setAlarmForFutureAgendaItems() {}

    override suspend fun cancelAllAlarms() {}
}