package com.vero.tasky.agenda.data.local.dao

import androidx.room.*
import com.vero.tasky.agenda.data.local.entities.TaskEntity

interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTasks(vararg events: TaskEntity)

    @Update
    fun updateTasks(vararg events: TaskEntity)

    @Delete
    fun deleteTasks(vararg events: TaskEntity)

    @Transaction
    @Query("""
        SELECT *
        FROM task
        WHERE DATE(:timestamp) = CURDATE()
    """)
    fun loadTasksForDay(timestamp: Long) : List<TaskEntity>

}