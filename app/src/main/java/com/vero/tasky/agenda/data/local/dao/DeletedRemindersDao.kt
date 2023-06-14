package com.vero.tasky.agenda.data.local.dao

import androidx.room.*
import com.vero.tasky.agenda.data.local.entities.DeletedReminderEntity

@Dao
interface DeletedRemindersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReminder(vararg events: DeletedReminderEntity)

    @Delete
    fun deleteReminder(vararg events: DeletedReminderEntity)

    @Query("""
        SELECT *
        FROM deleted_reminder
    """)
    fun loadReminders() : List<DeletedReminderEntity>
}