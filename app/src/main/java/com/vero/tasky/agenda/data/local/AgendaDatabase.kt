package com.vero.tasky.agenda.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vero.tasky.agenda.data.local.converter.Converters
import com.vero.tasky.agenda.data.local.dao.DeletedAgendaItemDao
import com.vero.tasky.agenda.data.local.dao.EventDao
import com.vero.tasky.agenda.data.local.dao.ReminderDao
import com.vero.tasky.agenda.data.local.dao.TaskDao
import com.vero.tasky.agenda.data.local.entities.*

@Database(entities = [EventEntity::class, ReminderEntity::class, TaskEntity::class,
    LocalPhotoEntity::class, RemotePhotoEntity::class, DeletedAgendaItemEntity::class,
    AttendeeEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AgendaDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun taskDao(): TaskDao
    abstract fun reminderDao(): ReminderDao
    abstract fun deletedAgendaItemDao(): DeletedAgendaItemDao
}