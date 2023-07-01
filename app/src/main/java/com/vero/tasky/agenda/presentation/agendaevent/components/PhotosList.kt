package com.vero.tasky.agenda.presentation.agendaevent.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vero.tasky.R
import com.vero.tasky.agenda.domain.model.AgendaPhoto
import com.vero.tasky.ui.theme.*

@Composable
fun PhotoList(
    photos: List<AgendaPhoto>,
    onPhotoClick: (AgendaPhoto) -> Unit,
    onAddPhotoClick: () -> Unit,
    isAddPhotoVisible: Boolean,
    modifier: Modifier = Modifier,
) {
    if (photos.isEmpty()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = modifier
                .height(110.dp)
                .fillMaxWidth()
                .background(reminderBackgroundColor)
                .clickable { onAddPhotoClick() }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(id = R.string.add),
                tint = MaterialTheme.colors.onTextFieldIcon
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(id = R.string.add_photo),
                color = MaterialTheme.colors.onTextFieldIcon,
                style = MaterialTheme.typography.Inter600Size16
            )
        }
    } else {
        Column {
            Text(
                text = stringResource(id = R.string.photos),
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.Inter600Size20
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow{
                items(photos) {
                    Box(modifier = Modifier
                        .padding(8.dp)
                        .aspectRatio(1f)) {
                        PhotoListItem(photo = it, onPhotoClick = onPhotoClick)
                    }
                }
                if (isAddPhotoVisible) {
                    item {
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .aspectRatio(1f)
                        ) {
                            AddPhotoListItem(onAddPhotoClick = onAddPhotoClick)
                        }
                    }
                }
            }
        }
    }
}