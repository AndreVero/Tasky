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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vero.tasky.R
import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.presentation.components.CircleCheckBox
import com.vero.tasky.agenda.presentation.util.AgendaTimeParser
import com.vero.tasky.ui.theme.*

@Composable
fun AgendaComponent(
    agendaItem: AgendaItem,
    modifier: Modifier = Modifier,
    onOpenClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onCheckChanged: (agendaItem: AgendaItem.Task) -> Unit,
    isCurrent: Boolean
) {

    var isMoreDropDownVisible by remember { mutableStateOf(false) }

    var isDeleteAgendaItemDialogVisible by remember {
        mutableStateOf(false)
    }

    val backgroundColor = when(agendaItem) {
        is AgendaItem.Reminder -> reminderBackgroundColor
        is AgendaItem.Task -> taskBackgroundColor
        is AgendaItem.Event -> eventBackgroundColor
    }

    val textColor = when(agendaItem) {
        is AgendaItem.Task -> Color.White
        else -> MaterialTheme.colors.text
    }

    if (isDeleteAgendaItemDialogVisible) {
        DeleteAgendaItemDialog(
            onDismissRequest = { isDeleteAgendaItemDialogVisible = false },
            onYesClick = { onDeleteClick() },
            onNoClick = { isDeleteAgendaItemDialogVisible = false })
    }

    Column(modifier = modifier
        .clickable { onOpenClick() }
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
            Column(modifier = Modifier.align(Alignment.CenterEnd)) {
                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = stringResource(id = R.string.more),
                    tint = textColor,
                    modifier = Modifier.clickable { isMoreDropDownVisible = true }
                )
                if (isMoreDropDownVisible) {
                    DefaultDropDownMenu(
                        actions = hashMapOf(
                            R.string.open to { onOpenClick() },
                            R.string.edit to { onEditClick() },
                            R.string.delete to { isDeleteAgendaItemDialogVisible = true }
                        ),
                        onDismissRequest = { isMoreDropDownVisible = false}
                    )
                }
            }

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