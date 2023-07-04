package com.vero.tasky.agenda.data.local.dao

import androidx.room.*
import com.vero.tasky.agenda.data.local.entities.ReminderEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface ReminderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminders(vararg reminders: ReminderEntity)

    @Update
    suspend fun updateReminders(vararg reminders: ReminderEntity)

    @Delete
    suspend fun deleteReminders(vararg reminders: ReminderEntity)

    @Query("""
        SELECT *
        FROM reminder
        WHERE time BETWEEN :from AND :to
    """)
    fun loadRemindersForDay(from: Long, to: Long) : Flow<List<ReminderEntity>>

    @Query("""
        SELECT *
        FROM reminder
    """)
    fun loadAllReminders() : List<ReminderEntity>

}