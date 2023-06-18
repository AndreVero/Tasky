package com.vero.tasky.agenda.presentation.agenda.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vero.tasky.ui.theme.profileText

@Composable
fun ProfileIcon(
    text: String,
    color: Color,
    modifier: Modifier
) {
    Text(
        modifier = modifier
            .padding(16.dp)
            .drawBehind {
                drawCircle(
                    color = color,
                    radius = this.size.maxDimension
                )
            },
        color = MaterialTheme.colors.profileText,
        text = text.uppercase(),
        style = MaterialTheme.typography.h3
    )
}