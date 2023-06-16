package com.vero.tasky.agenda.presentation.agenda.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vero.tasky.agenda.domain.model.Day
import java.time.format.TextStyle
import java.util.*

@Composable
fun DayComponent(
    day: Day,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = day.date.dayOfMonth.toString())
        Spacer(modifier = Modifier.padding(16.dp))
        Text(text = day.date.dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.getDefault()))
    }
}