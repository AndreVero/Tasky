package com.vero.tasky.agenda.presentation.agenda

import android.os.Parcelable
import com.vero.tasky.agenda.domain.model.AgendaItem
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class AgendaState(
    val userShortName: String = "",
    val monthLabel: String = "",
    val days: List<LocalDate> = emptyList(),
    val dayLabel: String = "",
    val agendaItems: List<AgendaItem> = emptyList()
) : Parcelable
