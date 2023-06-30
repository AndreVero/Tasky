package com.vero.tasky.agenda.presentation.agendaevent.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vero.tasky.R
import com.vero.tasky.ui.theme.onTextFieldIcon

@Composable
fun AddPhotoListItem(
    modifier: Modifier = Modifier,
    onAddPhotoClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(80.dp)
            .width(80.dp)
            .border(2.dp, MaterialTheme.colors.onTextFieldIcon, RoundedCornerShape(5.dp))
            .clickable { onAddPhotoClick() }
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(id = R.string.add_photo_button),
            tint = MaterialTheme.colors.onTextFieldIcon,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}