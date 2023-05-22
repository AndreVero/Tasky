package com.vero.tasky.core.presentation.components

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle

@Composable
fun ProgressBarText(
    isLoading: Boolean,
    textRes: Int,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    allCaps: Boolean = true,
) {
    val text = stringResource(id = textRes)

    if (isLoading) {
        CircularProgressIndicator(
            color = Color.White,
            modifier = modifier
        )
    } else {
        Text(
            text = if (allCaps) text.uppercase() else text,
            style = textStyle,
            modifier = modifier
        )
    }
}