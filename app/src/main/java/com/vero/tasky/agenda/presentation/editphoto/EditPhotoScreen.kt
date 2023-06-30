package com.vero.tasky.agenda.presentation.editphoto

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.vero.tasky.R
import com.vero.tasky.ui.theme.Inter600Size20
import com.vero.tasky.ui.theme.headerText

@Composable
fun EditPhotoScreen(
    uri: String,
    navBack: () -> Unit,
    @StringRes label: Int,
    deletePhoto: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            IconButton(onClick = navBack) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(id = R.string.back),
                    tint = MaterialTheme.colors.headerText,
                )
            }
            Text(
                text = stringResource(id = label).uppercase(),
                style = MaterialTheme.typography.Inter600Size20,
                color = MaterialTheme.colors.headerText,
            )
            IconButton(onClick = { deletePhoto(uri) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(id = R.string.delete),
                    tint = MaterialTheme.colors.headerText,
                )
            }
        }
        AsyncImage(
            model = uri,
            contentDescription = stringResource(id = R.string.photo_for_editing),
            modifier = Modifier.fillMaxSize()
        )
    }
}