package com.vero.tasky.agenda.domain.workmanagerrunner

import com.vero.tasky.agenda.domain.model.ModificationType

interface SaveTaskRunner {

    fun run(taskId: String, modificationType: ModificationType)

    companion object {
        const val TASK_ID = "TASK_ID"
        const val MODIFICATION_TYPE = "MODIFICATION_TYPE"
    }

}