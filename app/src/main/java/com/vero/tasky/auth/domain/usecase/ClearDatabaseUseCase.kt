package com.vero.tasky.auth.domain.usecase

import com.vero.tasky.agenda.data.local.AgendaDatabase
import javax.inject.Inject

class ClearDatabaseUseCase @Inject constructor(
    private val db: AgendaDatabase
) {

    operator fun invoke() {
        db.clearAllTables()
    }
}