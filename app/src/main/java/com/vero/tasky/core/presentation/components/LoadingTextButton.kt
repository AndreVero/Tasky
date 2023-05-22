package com.vero.tasky.core.presentation.components

import androidx.compose.foundation.layout.padding
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
    textRes: Int,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    onClick : () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(38.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.buttonBackground,
            contentColor = MaterialTheme.colors.buttonText
        ),
        modifier = modifier
    ) {
        ProgressBarText(
            isLoading = isLoading,
            textRes = textRes,
            textStyle = MaterialTheme.typography.h3,
            modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
        )
    }
}