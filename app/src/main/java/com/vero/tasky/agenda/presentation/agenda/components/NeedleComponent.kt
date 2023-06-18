package com.vero.tasky.agenda.presentation.agenda.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun NeedleComponent(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.background
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Canvas(
            modifier = Modifier
                .height(15.dp)
                .width(15.dp),
            onDraw = {
                drawCircle(color)
            })
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(color))
    }
}