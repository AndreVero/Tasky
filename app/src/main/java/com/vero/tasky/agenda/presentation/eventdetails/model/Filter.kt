package com.vero.tasky.agenda.presentation.eventdetails.model

import androidx.annotation.StringRes
import com.vero.tasky.R


enum class Filter(@StringRes val label: Int) {
    ALL(R.string.all),
    GOING(R.string.going),
    NOT_GOING(R.string.not_going)
}
