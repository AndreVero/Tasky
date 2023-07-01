package com.vero.tasky.agenda.data.local.dao

import androidx.room.*
import com.vero.tasky.agenda.data.local.entities.TaskEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(vararg tasks: TaskEntity)

    @Update
    suspend fun updateTasks(vararg tasks: TaskEntity)

    @Delete
    suspend fun deleteTasks(vararg tasks: TaskEntity)

    @Query("""
        SELECT *
        FROM task
        WHERE time BETWEEN :from AND :to
    """)
    fun loadTasksForDay(from: Long, to: Long) : Flow<List<TaskEntity>>

}