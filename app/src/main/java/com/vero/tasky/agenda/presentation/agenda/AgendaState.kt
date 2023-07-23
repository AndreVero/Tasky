package com.vero.tasky.agenda.presentation.agenda

import android.os.Parcelable
import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.presentation.agenda.model.Day
import kotlinx.parcelize.Parcelize

@Parcelize
data class AgendaState(
    val userShortName: String = "",
    val monthLabel: String = "",
    val days: List<Day> = emptyList(),
    val dayLabel: String = "",
    val currentAgendaItem: AgendaItem? = null,
    val agendaItems: List<AgendaItem> = emptyList(),
    val isRefreshing: Boolean = false
) : Parcelable
