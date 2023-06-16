package com.vero.tasky.agenda.presentation.agenda.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.presentation.util.AgendaTimeParser

@Composable
fun AgendaComponent(
    agendaItem: AgendaItem,
    modifier: Modifier = Modifier,
    isCurrent: Boolean
) {

    val backgroundColor = when(agendaItem) {
        is AgendaItem.Reminder -> MaterialTheme.colors.background
        is AgendaItem.Task -> MaterialTheme.colors.background
        is AgendaItem.Event -> MaterialTheme.colors.background
    }

    Column(modifier = modifier
        .background(backgroundColor)
    ) {
        Row {
            CircleCheckBox(isChecked = false)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = agendaItem.title,
                style = MaterialTheme.typography.h2
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = agendaItem.description ?: "",
            maxLines = 2,
            style = MaterialTheme.typography.body2
        )
        Text(
            text = AgendaTimeParser.getTimeLine(agendaItem),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.body2
        )
    }
    if (isCurrent) {
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(1.dp))
    }
}