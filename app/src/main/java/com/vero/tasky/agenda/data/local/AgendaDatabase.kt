package com.vero.tasky.agenda.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vero.tasky.agenda.data.local.dao.*
import com.vero.tasky.agenda.data.local.entities.*

@Database(entities = [AttendeeEntity::class, CreatedAttendeeEntity::class, CreatedEventEntity::class,
    CreatedLocalPhotoEntity::class, CreatedReminderEntity::class, CreatedTaskEntity::class,
    DeletedEventEntity::class, DeletedLocalPhotoEntity::class, DeletedReminderEntity::class,
    DeletedTaskEntity::class, EventEntity::class, ReminderEntity::class,
    RemotePhotoEntity::class, TaskEntity::class, UpdatedAttendeeEntity::class,
    UpdatedEventEntity::class, UpdatedLocalPhotoEntity::class, UpdatedReminderEntity::class,
    UpdatedTaskEntity::class], version = 1)
abstract class AgendaDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun taskDao(): TaskDao
    abstract fun reminderDao(): ReminderDao
    abstract fun deletedTasksDao(): DeletedTasksDao
    abstract fun deletedRemindersDao(): DeletedRemindersDao
    abstract fun deletedEventsDao(): DeletedEventsDao
}