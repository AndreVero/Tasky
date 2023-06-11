package com.vero.tasky.agenda.presentation.agenda

import android.os.Parcelable
import com.vero.tasky.agenda.domain.model.AgendaItem
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class AgendaState(
    val userShortName: String = "",
    val month: String = "",
    val days: List<LocalDateTime> = emptyList(),
    val dayLabel: String = "",
    val agendaItems: List<AgendaItem> = emptyList()
) : Parcelable
