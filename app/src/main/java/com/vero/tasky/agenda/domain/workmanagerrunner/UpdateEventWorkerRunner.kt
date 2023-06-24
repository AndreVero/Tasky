package com.vero.tasky.agenda.domain.workmanagerrunner

interface UpdateEventWorkerRunner {

    fun run(isGoing: Boolean)

    companion object {
        const val IS_GOING = "IS_GOING"
    }

}