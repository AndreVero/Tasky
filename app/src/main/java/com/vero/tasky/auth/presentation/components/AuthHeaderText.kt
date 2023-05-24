package com.vero.tasky.auth.presentation.components

import androidx.annotation.StringRes
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vero.tasky.ui.theme.headerText

@Composable
fun AuthHeaderText(
    @StringRes titleRes: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = titleRes),
        style = MaterialTheme.typography.h1,
        color = MaterialTheme.colors.headerText,
        modifier = modifier
    )
}