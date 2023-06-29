package com.vero.tasky.agenda.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vero.tasky.ui.theme.profileIcon

@Composable
fun BaseLine(
    modifier: Modifier = Modifier
) {
    Spacer(modifier = modifier
        .height(1.dp)
        .fillMaxWidth()
        .background(MaterialTheme.colors.profileIcon)
    )
}