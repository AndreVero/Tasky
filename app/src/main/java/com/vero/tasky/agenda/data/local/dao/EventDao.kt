package com.vero.tasky.agenda.data.local.dao

import androidx.room.*
import com.vero.tasky.agenda.data.local.EventWithPhotosAndAttendees
import com.vero.tasky.agenda.data.local.entities.EventEntity
import kotlinx.coroutines.flow.Flow

interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEvents(vararg events: EventEntity)

    @Update
    fun updateEvents(vararg events: EventEntity)

    @Delete
    fun deleteEvents(vararg events: EventEntity)

    @Transaction
    @Query("""
        SELECT *
        FROM event
        WHERE DATE(:timestamp) = CURDATE()
    """)
    fun loadEventsForDay(timestamp: Long) : List<EventWithPhotosAndAttendees>

}