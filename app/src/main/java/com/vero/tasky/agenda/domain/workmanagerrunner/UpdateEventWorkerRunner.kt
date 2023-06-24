package com.vero.tasky.agenda.domain.workmanagerrunner

interface UpdateEventWorkerRunner {

    fun run(isGoing: Boolean, eventId: String)

    companion object {
        const val EVENT_ID = "EVENT_ID"
        const val IS_GOING = "IS_GOING"
    }

}