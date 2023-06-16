package com.vero.tasky.agenda.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class Day(
    val date: LocalDate,
    val isSelected: Boolean
) : Parcelable
