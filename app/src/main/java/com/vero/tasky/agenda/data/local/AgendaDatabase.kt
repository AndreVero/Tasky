package com.vero.tasky.agenda.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vero.tasky.agenda.data.local.dao.EventDao
import com.vero.tasky.agenda.data.local.dao.ReminderDao
import com.vero.tasky.agenda.data.local.dao.TaskDao
import com.vero.tasky.agenda.data.local.entities.EventEntity
import com.vero.tasky.agenda.data.local.entities.ReminderEntity
import com.vero.tasky.agenda.data.local.entities.TaskEntity

@Database(entities = [EventEntity::class, ReminderEntity::class, TaskEntity::class], version = 1)
abstract class AgendaDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun taskDao(): TaskDao
    abstract fun reminderDao(): ReminderDao
}