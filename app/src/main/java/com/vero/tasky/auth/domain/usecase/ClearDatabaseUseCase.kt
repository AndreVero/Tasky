package com.vero.tasky.auth.domain.usecase

import com.vero.tasky.agenda.data.local.AgendaDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ClearDatabaseUseCase @Inject constructor(
    private val db: AgendaDatabase
) {

    suspend operator fun invoke() {
        withContext(Dispatchers.IO) {
            db.clearAllTables()
        }
    }
}