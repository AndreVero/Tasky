package com.vero.tasky.agenda.data.local.dao

import androidx.room.*
import com.vero.tasky.agenda.data.local.entities.TaskEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTasks(vararg events: TaskEntity)

    @Update
    fun updateTasks(vararg events: TaskEntity)

    @Delete
    fun deleteTasks(vararg events: TaskEntity)

    @Query("""
        SELECT *
        FROM task
        WHERE DATE(:timestamp) = DATE()
    """)
    fun loadTasksForDay(timestamp: Long) : Flow<List<TaskEntity>>

}