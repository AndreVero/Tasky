package com.vero.tasky.agenda.data.local.dao

import androidx.room.*
import com.vero.tasky.agenda.data.local.EventWithPhotosAndAttendees
import com.vero.tasky.agenda.data.local.entities.AttendeeEntity
import com.vero.tasky.agenda.data.local.entities.EventEntity
import com.vero.tasky.agenda.data.local.entities.LocalPhotoEntity
import com.vero.tasky.agenda.data.local.entities.RemotePhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(vararg events: EventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendees(vararg attendees: AttendeeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemotePhotoEntity(vararg photos: RemotePhotoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalPhotoEntity(vararg photos: LocalPhotoEntity)

    @Update
    suspend fun updateEvents(vararg events: EventEntity)

    @Delete
    suspend fun deleteEvents(vararg events: EventEntity)

    @Transaction
    @Query("""
        SELECT *
        FROM event
        WHERE DATE(:timestamp) = DATE()
    """)
    fun loadEventsForDay(timestamp: Long) : Flow<List<EventWithPhotosAndAttendees>>

    @Transaction
    @Query("SELECT * FROM event WHERE id = :id")
    fun loadEvent(id: String) : Flow<EventWithPhotosAndAttendees>

}