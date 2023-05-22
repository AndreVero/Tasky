package com.vero.tasky.core.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vero.tasky.ui.theme.onTextFieldHint
import com.vero.tasky.ui.theme.text
import com.vero.tasky.ui.theme.textFieldBackground

@Composable
fun VerifiableTextField(
    isValid: Boolean,
    hintRes: Int,
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
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
       trailingIcon = {
           if (isValid) 
               Icon(
                   imageVector = Icons.Default.Check,
                   contentDescription = "Field is valid",
                   tint = MaterialTheme.colors.primary
               )
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