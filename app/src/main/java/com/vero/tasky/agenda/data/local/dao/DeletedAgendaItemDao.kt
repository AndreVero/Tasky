package com.vero.tasky.agenda.data.local.dao

import androidx.room.*
import com.vero.tasky.agenda.data.local.entities.DeletedAgendaItemEntity

@Dao
interface DeletedAgendaItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAgendaItem(vararg events: DeletedAgendaItemEntity)

    @Delete
    fun deleteAgendaItem(vararg events: DeletedAgendaItemEntity)

    @Transaction
    @Query("""
        SELECT *
        FROM deleted_item
    """)
    fun loadDeletedAgendaItems() : List<DeletedAgendaItemEntity>
}