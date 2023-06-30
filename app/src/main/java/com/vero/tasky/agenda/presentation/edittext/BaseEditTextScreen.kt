package com.vero.tasky.agenda.presentation.edittext

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.vero.tasky.R
import com.vero.tasky.agenda.presentation.components.BaseLine
import com.vero.tasky.ui.theme.*

@Composable
fun EditTextScreen(
    @StringRes label: Int,
    textValue: String,
    textStyle: TextStyle,
    navBack: () -> Unit,
    save: (String) -> Unit,
) {

    var text by remember {
        mutableStateOf(textValue)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.primaryVariant)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            IconButton(onClick = navBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.back),
                    tint = MaterialTheme.colors.primary,
                )
            }
            Text(
                text = stringResource(id = label).uppercase(),
                style = MaterialTheme.typography.Inter600Size20,
                color = MaterialTheme.colors.primary
            )
            Text(
                text = stringResource(id = R.string.save),
                style = MaterialTheme.typography.Inter600Size16,
                color = taskBackgroundColor,
                modifier = Modifier.clickable { save(text) }
            )
        }
        BaseLine()
        TextField(
            value = text,
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colors.primary,
                backgroundColor = MaterialTheme.colors.primaryVariant
            ),
            onValueChange = { text = it },
            textStyle = textStyle,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primaryVariant)
        )
    }
}