package com.vero.tasky.auth.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.vero.tasky.ui.theme.onTextFieldHint
import com.vero.tasky.ui.theme.onTextFieldIcon
import com.vero.tasky.ui.theme.text
import com.vero.tasky.ui.theme.textFieldBackground

@Composable
fun PasswordTextField(
    hintRes: Int,
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isPasswordVisible: Boolean,
    onPasswordVisibilityUpdate: () -> Unit
) {

    TextField(
        value = text,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = stringResource(id = hintRes),
                color = MaterialTheme.colors.onTextFieldHint
            )
        },
        textStyle = MaterialTheme.typography.body1,
        singleLine = true,
        visualTransformation = if (isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            if (!isPasswordVisible) {
                IconButton(onClick = onPasswordVisibilityUpdate) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        tint = MaterialTheme.colors.onTextFieldIcon,
                        contentDescription = "Visibility"
                    )
                }
            } else {
                IconButton(onClick = onPasswordVisibilityUpdate) {
                    Icon(
                        imageVector = Icons.Filled.VisibilityOff,
                        tint = MaterialTheme.colors.onTextFieldIcon,
                        contentDescription = "Visibility off"
                    )
                }
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colors.text,
            backgroundColor = MaterialTheme.colors.textFieldBackground,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
    )
}