package com.vero.tasky.agenda.presentation.agenda.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class Day(
    val date: LocalDate,
    val isSelected: Boolean
) : Parcelable
