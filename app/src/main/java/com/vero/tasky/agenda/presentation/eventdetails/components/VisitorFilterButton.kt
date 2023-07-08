package com.vero.tasky.agenda.presentation.eventdetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vero.tasky.agenda.presentation.eventdetails.model.Filter
import com.vero.tasky.ui.theme.textFieldBackground

@Composable
fun VisitorFilterButton(
    filter: Filter,
    isSelected: Boolean,
    onFilterClick: (Filter) -> Unit,
    modifier: Modifier = Modifier,
) {
    val textColor = if (isSelected) Color.White
        else MaterialTheme.colors.primary

    val backgroundColor = if (isSelected) MaterialTheme.colors.primary
        else MaterialTheme.colors.textFieldBackground

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .width(100.dp)
            .height(30.dp)
            .background(backgroundColor)
            .clickable { onFilterClick(filter) }
        ,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = filter.label),
            color = textColor,
            textAlign = TextAlign.Center,
        )
    }

}