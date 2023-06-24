package com.vero.tasky.agenda.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vero.tasky.agenda.data.local.dao.*
import com.vero.tasky.agenda.data.local.entities.*

@Database(entities = [AttendeeEntity::class, ModifiedAgendaItemEntity::class,
    LocalPhotoEntity::class, EventEntity::class, ReminderEntity::class,
    RemotePhotoEntity::class, TaskEntity::class, DeletedPhotoEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AgendaDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun taskDao(): TaskDao
    abstract fun reminderDao(): ReminderDao
    abstract fun modifiedAgendaItemDao(): ModifiedAgendaItemDao
}