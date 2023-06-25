package com.vero.tasky.agenda.domain.remindermanager

interface ReminderManager {

    fun setReminder(data: ReminderData)
    fun cancelReminder(id: String)

    companion object {
        const val CHANNEL_ID = "reminders_channel"
        const val ITEM_ID = "item_id"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
    }
}