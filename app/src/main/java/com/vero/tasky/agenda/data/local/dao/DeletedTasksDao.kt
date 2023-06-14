package com.vero.tasky.agenda.data.local.dao

import androidx.room.*
import com.vero.tasky.agenda.data.local.entities.DeletedTaskEntity

@Dao
interface DeletedTasksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(vararg events: DeletedTaskEntity)

    @Delete
    fun deleteTask(vararg events: DeletedTaskEntity)

    @Query("""
        SELECT *
        FROM deleted_event
    """)
    fun loadTasks() : List<DeletedTaskEntity>
}