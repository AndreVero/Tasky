package com.vero.tasky.auth.presentation.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.vero.tasky.ui.theme.headerText

@Composable
fun AuthHeaderText(
    titleRes: Int
) {
    Text(
        text = stringResource(id = titleRes),
        style = MaterialTheme.typography.h1,
        color = MaterialTheme.colors.headerText,
    )
}