package com.vero.tasky.agenda.domain.remindermanager

interface AlarmHandler {

    fun setAlarm(data: AlarmData)
    fun cancelAlarm(id: String)

    companion object {
        const val CHANNEL_ID = "reminders_channel"
        const val ITEM_ID = "item_id"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
    }
}