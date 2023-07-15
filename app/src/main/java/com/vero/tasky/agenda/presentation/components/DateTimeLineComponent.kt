package com.vero.tasky.agenda.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vero.tasky.agenda.presentation.util.LocalDateParser
import com.vero.tasky.ui.theme.Inter400Size16
import java.time.LocalDateTime
import java.util.*

@Composable
fun DateTimeLineComponent(
    @StringRes label: Int,
    isEditable: Boolean,
    localDateTime: LocalDateTime,
    onEditTimeClick: () -> Unit,
    onEditDateClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row (
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TimePickerLabelComponent(
            label = label,
            value = LocalDateParser.getTimeLabel(localDateTime),
            isEditable = isEditable,
            onEditClick = onEditTimeClick
        )
        AgendaItemTextComponent(
            text = LocalDateParser.getShortDayLabel(localDateTime, Locale.getDefault()),
            isEditable = isEditable,
            onEditClick = onEditDateClick,
            modifier = Modifier.width(160.dp),
            textStyle = MaterialTheme.typography.Inter400Size16
        )
    }
}