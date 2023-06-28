package com.vero.tasky.agenda.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import com.vero.tasky.ui.theme.Inter600Size16
import com.vero.tasky.ui.theme.text

@Composable
fun AgendaItemTypeComponent(
    color: Color,
    @StringRes type: Int,
    modifier: Modifier = Modifier
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Spacer(modifier = Modifier
            .width(20.dp)
            .height(20.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(color)
        )
        Spacer(modifier = modifier.width(12.dp))
        Text(
            text = stringResource(id = type),
            style = MaterialTheme.typography.Inter600Size16,
            color = MaterialTheme.colors.text
        )
    }
}