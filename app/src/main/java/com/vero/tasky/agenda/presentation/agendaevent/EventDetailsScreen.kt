package com.vero.tasky.agenda.presentation.agendaevent

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.vero.tasky.R
import com.vero.tasky.agenda.presentation.components.BaseAgendaScreen
import com.vero.tasky.agenda.presentation.util.LocalDateParser
import com.vero.tasky.core.presentation.components.LocalSnackbarHostState
import com.vero.tasky.core.presentation.components.ProgressBarText
import com.vero.tasky.ui.theme.Inter600Size16
import com.vero.tasky.ui.theme.headerText
import kotlinx.coroutines.launch

@Composable
fun EventDetailsScreen(
    viewModel: EventDetailsViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = LocalSnackbarHostState.current

    val state = viewModel.state

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEventDetailsEvent.ShowErrorMessage -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = context.getString(event.message),
                        )
                    }
                }
                UiEventDetailsEvent.OnBackClick -> navigateBack()
                is UiEventDetailsEvent.ShowInfoToast -> {
                    Toast.makeText(context, event.text, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    BaseAgendaScreen(
        headerContent = {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(id = R.string.close),
                tint = MaterialTheme.colors.headerText,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clickable { navigateBack() },
            )
            Text(
                text = LocalDateParser.getDayLabel(state.agendaItem.time.toLocalDate()),
                style = MaterialTheme.typography.Inter600Size16,
                modifier = Modifier.align(Alignment.Center)
            )
            if (state.isEditable) {
                ProgressBarText(
                    isLoading = state.isLoading,
                    textRes = R.string.save,
                    textStyle = MaterialTheme.typography.Inter600Size16,
                    modifier = Modifier.clickable { viewModel.onEvent(EventDetailsEvent.SaveEvent) }
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(id = R.string.edit),
                    tint = MaterialTheme.colors.headerText,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable { viewModel.onEvent(EventDetailsEvent.ChangeMode) }
                )
            }
        },
        bodyContent = {

        }
    )
}