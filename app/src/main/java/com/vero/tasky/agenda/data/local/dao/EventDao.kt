package com.vero.tasky.agenda.data.local.dao

import androidx.room.*
import com.vero.tasky.agenda.data.local.EventWithPhotosAndAttendees
import com.vero.tasky.agenda.data.local.entities.AttendeeEntity
import com.vero.tasky.agenda.data.local.entities.EventEntity
import com.vero.tasky.agenda.data.local.entities.RemotePhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEvents(vararg events: EventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAttendees(vararg events: AttendeeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRemotePhotoEntity(vararg events: RemotePhotoEntity)

    @Update
    fun updateEvents(vararg events: EventEntity)

    @Delete
    fun deleteEvents(vararg events: EventEntity)

    @Transaction
    @Query("""
        SELECT *
        FROM event
        WHERE DATE(:timestamp) = DATE()
    """)
    fun loadEventsForDay(timestamp: Long) : Flow<List<EventWithPhotosAndAttendees>>

}