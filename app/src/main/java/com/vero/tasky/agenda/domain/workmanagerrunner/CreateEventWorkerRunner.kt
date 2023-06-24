package com.vero.tasky.agenda.domain.workmanagerrunner

interface CreateEventWorkerRunner {

    fun run(eventId: String)

    companion object {
        const val EVENT_ID = "EVENT_ID"
    }

}