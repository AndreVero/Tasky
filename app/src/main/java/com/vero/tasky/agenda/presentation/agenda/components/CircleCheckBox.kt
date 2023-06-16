package com.vero.tasky.agenda.presentation.agenda.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp

@Composable
fun CircleCheckBox(
    isChecked: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .size(21.dp)
            .background(Color.Transparent)
            .padding(1.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        if (isChecked)
            Icon(imageVector = Icons.Default.Check, contentDescription = "Check box")
    }
}