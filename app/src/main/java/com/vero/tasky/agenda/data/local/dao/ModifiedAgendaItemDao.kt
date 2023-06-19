package com.vero.tasky.agenda.data.local.dao

import androidx.room.*
import com.vero.tasky.agenda.data.local.entities.ModifiedAgendaItemEntity

@Dao
interface ModifiedAgendaItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAgendaItem(vararg events: ModifiedAgendaItemEntity)

    @Delete
    suspend fun deleteAgendaItem(vararg events: ModifiedAgendaItemEntity)

    @Query("""
        SELECT *
        FROM modified_agenda_item
        WHERE modificationType = 'deleted'
    """)
    suspend fun loadDeletedAgendaItems() : List<ModifiedAgendaItemEntity>
}