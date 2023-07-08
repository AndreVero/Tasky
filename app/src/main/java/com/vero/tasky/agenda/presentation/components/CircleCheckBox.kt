package com.vero.tasky.agenda.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vero.tasky.R
import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.ui.theme.taskBackgroundColor

@Composable
fun CircleCheckBox(
    agendaItem: AgendaItem.Task,
    onCheckChanged: (task: AgendaItem.Task) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = taskBackgroundColor
) {
    Box(
        modifier = modifier
            .clickable { onCheckChanged(agendaItem) }
            .clip(CircleShape)
            .size(21.dp)
            .background(MaterialTheme.colors.background)
            .padding(1.dp)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        if (agendaItem.isDone)
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = stringResource(id = R.string.is_done),
                tint = MaterialTheme.colors.background
            )
    }
}