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

@Composable
fun DateTimeLineComponent(
    @StringRes label: Int,
    isEditable: Boolean,
    localDateTime: LocalDateTime,
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
            onEditClick = { }
        )
        AgendaItemTextComponent(
            text = LocalDateParser.getShortDayLabel(localDateTime),
            isEditable = isEditable,
            onEditClick = { },
            modifier = Modifier.width(160.dp),
            textStyle = MaterialTheme.typography.Inter400Size16
        )
    }
}