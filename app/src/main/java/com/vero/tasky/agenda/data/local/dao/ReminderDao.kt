package com.vero.tasky.agenda.data.local.dao

import androidx.room.*
import com.vero.tasky.agenda.data.local.entities.ReminderEntity
import kotlinx.coroutines.flow.Flow

interface ReminderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReminders(vararg events: ReminderEntity)

    @Update
    fun updateReminders(vararg events: ReminderEntity)

    @Delete
    fun deleteReminders(vararg events: ReminderEntity)

    @Transaction
    @Query("SELECT * FROM reminder WHERE DATE(:timestamp) = CURDATE()")
    fun loadRemindersForDay(timestamp: Long) : Flow<List<ReminderEntity>>

}