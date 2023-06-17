package com.vero.tasky.agenda.presentation.agenda.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.presentation.util.AgendaTimeParser
import com.vero.tasky.ui.theme.*

@Composable
fun AgendaComponent(
    agendaItem: AgendaItem,
    modifier: Modifier = Modifier,
    onOpenClick: (agendaItem: AgendaItem) -> Unit,
    onEditClick: (agendaItem: AgendaItem) -> Unit,
    onDeleteClick: (agendaItem: AgendaItem) -> Unit,
    onCheckChanged: (agendaItem: AgendaItem.Task) -> Unit,
    isCurrent: Boolean
) {

    val backgroundColor = when(agendaItem) {
        is AgendaItem.Reminder -> MaterialTheme.colors.reminder
        is AgendaItem.Task -> MaterialTheme.colors.task
        is AgendaItem.Event -> MaterialTheme.colors.event
    }

    val textColor = when(agendaItem) {
        is AgendaItem.Task -> Color.White
        else -> MaterialTheme.colors.text
    }

    Column(modifier = modifier
        .clickable { onOpenClick(agendaItem) }
        .padding(top = 8.dp, bottom = 8.dp)
        .clip(RoundedCornerShape(20.dp))
        .background(backgroundColor)
        .padding(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row (
                modifier = Modifier.align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (agendaItem is AgendaItem.Task) {
                    CircleCheckBox(agendaItem, onCheckChanged)
                    Spacer(modifier = Modifier.width(12.dp))
                }
                Text(
                    text = agendaItem.title,
                    style = MaterialTheme.typography.Inter700Size20,
                    color = textColor,
                )
            }
            Icon(
                imageVector = Icons.Default.MoreHoriz,
                contentDescription = "More",
                tint = textColor,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = agendaItem.description ?: "",
            maxLines = 2,
            style = MaterialTheme.typography.Inter400Size14,
            color = textColor
        )
        Text(
            text = AgendaTimeParser.getTimeLine(agendaItem),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.Inter400Size14,
            color = textColor
        )
    }
    if (isCurrent) {
        NeedleComponent()
    }
}