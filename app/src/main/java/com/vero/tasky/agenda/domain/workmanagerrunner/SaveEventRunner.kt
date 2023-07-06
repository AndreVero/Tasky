package com.vero.tasky.agenda.domain.workmanagerrunner

import com.vero.tasky.agenda.domain.model.ModificationType

interface SaveEventRunner {

    fun run(isGoing: Boolean, eventId: String, modificationType: ModificationType)

    companion object {
        const val EVENT_ID = "EVENT_ID"
        const val IS_GOING = "IS_GOING"
        const val MODIFICATION_TYPE = "MODIFICATION_TYPE"
    }

}