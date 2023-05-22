package com.vero.tasky.auth.presentation.components

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
fun BaseAuthScreen(
    titleRes: Int,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Column (
        modifier = modifier.background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(42.dp))
        AuthHeaderText(titleRes = titleRes)
        Spacer(modifier = Modifier.height(42.dp))
        Box (
            content = content,
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .fillMaxSize()
                .background(MaterialTheme.colors.primaryVariant)
                .padding(top = 50.dp, start = 16.dp, end = 16.dp)
        )
    }
}