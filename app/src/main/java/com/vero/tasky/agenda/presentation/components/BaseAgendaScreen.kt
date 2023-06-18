package com.vero.tasky.agenda.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun BaseAgendaScreen(
    modifier: Modifier = Modifier,
    bodyContent: @Composable BoxScope.() -> Unit,
    headerContent: @Composable BoxScope.() -> Unit,
) {
    Column (
        modifier = modifier.background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box (
            content = headerContent,
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .fillMaxWidth()
                .height(80.dp)
                .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
        )
        Box (
            content = bodyContent,
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .fillMaxHeight()
                .background(MaterialTheme.colors.primaryVariant)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        )
    }
}