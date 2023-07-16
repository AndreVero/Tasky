package com.vero.tasky.agenda.data.local.dao


import com.vero.tasky.agenda.data.local.entities.ModifiedAgendaItemEntity
import com.vero.tasky.agenda.domain.model.ModificationType

class ModifiedAgendaItemDaoFake : ModifiedAgendaItemDao {

    private val modifiedItemsSet = mutableSetOf<ModifiedAgendaItemEntity>()

    override suspend fun insertAgendaItem(vararg events: ModifiedAgendaItemEntity) {
        modifiedItemsSet.addAll(events)
    }

    override suspend fun deleteAgendaItem(vararg events: ModifiedAgendaItemEntity) {
        modifiedItemsSet.removeAll(events.toSet())
    }

    override suspend fun loadDeletedAgendaItems(): List<ModifiedAgendaItemEntity> {
        return modifiedItemsSet.filter { it.modificationType == ModificationType.DELETED }
    }

    override suspend fun loadCreatedAgendaItems(): List<ModifiedAgendaItemEntity> {
        return modifiedItemsSet.filter { it.modificationType == ModificationType.CREATED }
    }

    override suspend fun loadUpdatedAgendaItems(): List<ModifiedAgendaItemEntity> {
        return modifiedItemsSet.filter { it.modificationType == ModificationType.UPDATED }
    }
}