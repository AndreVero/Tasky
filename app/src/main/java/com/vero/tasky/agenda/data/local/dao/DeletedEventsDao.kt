package com.vero.tasky.agenda.data.local.dao

import androidx.room.*
import com.vero.tasky.agenda.data.local.entities.DeletedEventEntity

@Dao
interface DeletedEventsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEvent(vararg events: DeletedEventEntity)

    @Delete
    fun deleteEvent(vararg events: DeletedEventEntity)

    @Query("""
        SELECT *
        FROM deleted_task
    """)
    fun loadEvents() : List<DeletedEventEntity>
}