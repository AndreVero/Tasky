package com.vero.tasky.agenda.presentation.agenda.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.vero.tasky.agenda.domain.model.Day
import com.vero.tasky.ui.theme.*
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@Composable
fun DayComponent(
    day: Day,
    modifier: Modifier = Modifier,
    onClick: (date: LocalDate) -> Unit
) {
    val background =
        if (day.isSelected) MaterialTheme.colors.selectedDay
        else MaterialTheme.colors.day

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .background(background)
            .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
            .clickable { onClick(day.date) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = MaterialTheme.colors.text,
            style = MaterialTheme.typography.Inter700Size11
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = day.date.dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.getDefault()),
            color = MaterialTheme.colors.text,
            style = MaterialTheme.typography.Inter700Size17
        )
    }
}