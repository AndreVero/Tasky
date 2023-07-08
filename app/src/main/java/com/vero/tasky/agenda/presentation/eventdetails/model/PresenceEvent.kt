package com.vero.tasky.agenda.presentation.eventdetails.model

import androidx.annotation.StringRes
import com.vero.tasky.R

enum class PresenceEvent(@StringRes val label: Int) {
    DELETE(R.string.delete_event),
    LEAVE(R.string.leave_event),
    JOIN(R.string.join_event)
}