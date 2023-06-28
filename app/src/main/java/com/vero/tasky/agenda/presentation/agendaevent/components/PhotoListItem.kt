package com.vero.tasky.agenda.presentation.agendaevent.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.vero.tasky.R
import com.vero.tasky.agenda.domain.model.AgendaPhoto
import com.vero.tasky.ui.theme.profileIcon

@Composable
fun PhotoListItem(
    photo: AgendaPhoto,
    modifier: Modifier = Modifier,
    onPhotoClick: (AgendaPhoto) -> Unit,
) {
    AsyncImage(
        model = photo.path,
        contentDescription = stringResource(id = R.string.photo),
        modifier = modifier
            .height(60.dp)
            .width(60.dp)
            .border(2.dp,MaterialTheme.colors.profileIcon, RoundedCornerShape(5.dp))
            .clickable { onPhotoClick(photo) }
    )
}