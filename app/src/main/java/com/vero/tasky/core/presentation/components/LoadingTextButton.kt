package com.vero.tasky.core.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vero.tasky.ui.theme.buttonBackground
import com.vero.tasky.ui.theme.buttonText

@Composable
fun LoadingTextButton(
    @StringRes textRes: Int,
    isLoading: Boolean,
    onClick : () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(38.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.buttonBackground,
            contentColor = MaterialTheme.colors.buttonText
        ),
        contentPadding = PaddingValues(
            top = 12.dp,
            bottom = 12.dp,
            start = 8.dp,
            end = 8.dp
        ),
        modifier = modifier.height(55.dp)
    ) {
        ProgressBarText(
            isLoading = isLoading,
            textRes = textRes,
            textStyle = MaterialTheme.typography.h3,
        )
    }
}