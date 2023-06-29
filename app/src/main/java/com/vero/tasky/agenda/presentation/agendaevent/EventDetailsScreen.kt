package com.vero.tasky.agenda.presentation.agendaevent

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vero.tasky.R
import com.vero.tasky.agenda.presentation.agendaevent.components.*
import com.vero.tasky.agenda.presentation.components.*
import com.vero.tasky.agenda.presentation.util.LocalDateParser
import com.vero.tasky.core.presentation.components.LocalSnackbarHostState
import com.vero.tasky.core.presentation.components.ProgressBarText
import com.vero.tasky.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun EventDetailsScreen(
    viewModel: EventDetailsViewModel = hiltViewModel(),
    onEditTitle: (String) -> Unit,
    onEditDescription: (String) -> Unit,
    navigateBack: () -> Unit,
    title: String? = null,
    description: String? = null
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = LocalSnackbarHostState.current

    val state = viewModel.state

    LaunchedEffect(key1 = true) {
        viewModel.onEvent(EventDetailsEvent.CheckTitleAndDescription(title, description))
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
            if (state.isEditableForAttendee) {
                ProgressBarText(
                    isLoading = state.isLoading,
                    textRes = R.string.save,
                    textStyle = MaterialTheme.typography.Inter600Size16,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable { viewModel.onEvent(EventDetailsEvent.SaveEvent) }
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .scrollable(rememberScrollState(), Orientation.Vertical)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                AgendaItemTypeComponent(
                    color = eventBackgroundColor,
                    type = R.string.event,
                )
                Spacer(modifier = Modifier.height(16.dp))
                AgendaItemTextComponent(
                    text = state.agendaItem.title,
                    isEditable = state.isEditableForCreator,
                    onEditClick = { onEditTitle(state.agendaItem.title) },
                    textStyle = MaterialTheme.typography.Inter700Size26
                )
                Spacer(modifier = Modifier.height(16.dp))
                BaseLine()
                Spacer(modifier = Modifier.height(16.dp))
                AgendaItemTextComponent(
                    text = state.agendaItem.description ?: "",
                    isEditable = state.isEditableForCreator,
                    onEditClick = { onEditDescription(state.agendaItem.description ?: "") },
                    textStyle = MaterialTheme.typography.Inter400Size16
                )
                Spacer(modifier = Modifier.height(20.dp))
                PhotoList(
                    photos = state.agendaItem.photos,
                    onPhotoClick = { },
                    onAddPhotoClick = { },
                )
                Spacer(modifier = Modifier.height(20.dp))
                BaseLine()
                Spacer(modifier = Modifier.height(16.dp))
                DateTimeLineComponent(
                    isEditable = state.isEditableForCreator,
                    localDateTime = state.agendaItem.time,
                    label = R.string.from
                )
                Spacer(modifier = Modifier.height(16.dp))
                BaseLine()
                Spacer(modifier = Modifier.height(16.dp))
                DateTimeLineComponent(
                    isEditable = state.isEditableForCreator,
                    localDateTime = state.agendaItem.to,
                    label = R.string.to
                )
                Spacer(modifier = Modifier.height(16.dp))
                BaseLine()
                Spacer(modifier = Modifier.height(16.dp))
                ReminderComponent(
                    reminderRange = state.reminderRange,
                    onReminderClick = { },
                    isEditable = state.isEditableForAttendee
                )
                Spacer(modifier = Modifier.height(16.dp))
                BaseLine()
                Spacer(modifier = Modifier.height(16.dp))
                VisitorsLabelComponent(
                    onAddVisitorClick = { },
                    isEditable = state.isEditableForCreator
                )
                if (state.agendaItem.attendees.isNotEmpty()) {
                    AttendeesComponent(
                        isGoingAttendees = state.isGoingAttendees,
                        isNotGoingAttendees = state.isNotGoingAttendees,
                        isEditable = state.isEditableForCreator,
                        userId = state.agendaItem.host
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.delete_event).uppercase(),
                        style = MaterialTheme.typography.Inter600Size16,
                        color = MaterialTheme.colors.onTextFieldIcon,
                        modifier = Modifier.clickable {  }
                    )
                }
            }
        }
    )
}