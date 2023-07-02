package com.vero.tasky.agenda.presentation.agendaevent.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.vero.tasky.R
import com.vero.tasky.core.presentation.components.LoadingTextButton
import com.vero.tasky.core.presentation.components.VerifiableTextField
import com.vero.tasky.ui.theme.Inter700Size20

@Composable
fun AddAttendeeDialog(
    isLoading: Boolean,
    onDismissClick: () -> Unit,
    onAddEmail: () -> Unit,
    onEmailChanged: (String) -> Unit,
    email: String,
    isEmailValid: Boolean,
    isErrorEmail: Boolean,
    @StringRes emailLabel: Int,
    modifier: Modifier = Modifier,
) {

    Dialog(onDismissRequest = onDismissClick) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colors.primaryVariant)
                .padding(16.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = { onDismissClick() },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.close),
                        tint = MaterialTheme.colors.primary,
                    )
                }
            }
            Text(
                text = stringResource(id = R.string.add_visitor),
                style = MaterialTheme.typography.Inter700Size20,
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            VerifiableTextField(
                isValid = isEmailValid,
                label = emailLabel,
                isError = isErrorEmail,
                text = email,
                onValueChange = { email -> onEmailChanged(email) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            LoadingTextButton(
                textRes = R.string.add,
                isLoading = isLoading,
                onClick = { onAddEmail() },
                enabled = isEmailValid,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}