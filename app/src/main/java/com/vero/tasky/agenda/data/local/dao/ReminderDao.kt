package com.vero.tasky.agenda.data.local.dao

import androidx.room.*
import com.vero.tasky.agenda.data.local.entities.ReminderEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface ReminderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminders(vararg events: ReminderEntity)

    @Update
    suspend fun updateReminders(vararg events: ReminderEntity)

    @Delete
    suspend fun deleteReminders(vararg events: ReminderEntity)

    @Query("""
        SELECT *
        FROM reminder
        WHERE DATE(:timestamp) = DATE()
    """)
    fun loadRemindersForDay(timestamp: Long) : Flow<List<ReminderEntity>>

}