package com.vero.tasky.agenda.data.local.dao

import androidx.room.*
import com.vero.tasky.agenda.data.local.EventWithPhotosAndAttendees
import com.vero.tasky.agenda.data.local.entities.*
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeletedPhotoEntity(vararg photos: DeletedPhotoEntity)

    @Update
    suspend fun updateEvents(vararg events: EventEntity)

    @Delete
    suspend fun deleteEvents(vararg events: EventEntity)

    @Transaction
    @Query("""
        SELECT *
        FROM event
        WHERE time BETWEEN :from AND :to
    """)
    fun loadEventsForDay(from: Long, to: Long) : Flow<List<EventWithPhotosAndAttendees>>

    @Transaction
    @Query("SELECT * FROM event WHERE id = :id")
    fun loadEventFlow(id: String) : Flow<EventWithPhotosAndAttendees>

    @Transaction
    @Query("SELECT * FROM event WHERE id = :id")
    suspend fun loadEvent(id: String) : EventWithPhotosAndAttendees

}