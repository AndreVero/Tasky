package com.vero.tasky.agenda.presentation.agendaevent

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vero.tasky.R
import com.vero.tasky.agenda.presentation.agenda.components.DefaultConfirmationDialog
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
    onEditPhoto: (String, Boolean) -> Unit,
    navigateBack: () -> Unit,
    title: String? = null,
    description: String? = null,
    deletedPhotoUri: String? = null,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = LocalSnackbarHostState.current

    val dateFromDialog = rememberMaterialDialogState()
    val timeFromDialog = rememberMaterialDialogState()
    val dateToDialog = rememberMaterialDialogState()
    val timeToDialog = rememberMaterialDialogState()
    var presenceDialogVisible by remember { mutableStateOf(false) }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let { viewModel.onEvent(EventDetailsEvent.AddPhoto(uri = it)) }
        }
    )
    val state = viewModel.state

    LaunchedEffect(key1 = true) {
        viewModel.onEvent(
            EventDetailsEvent.CheckModifiedInfo(
                title = title,
                description = description,
                deletedPhotoUri = deletedPhotoUri
            )
        )
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

    DateDialog(dialogState = dateFromDialog, onDayPicked = {
        viewModel.onEvent(EventDetailsEvent.FromDateChanged(it))
    })
    TimeDialog(dialogState = timeFromDialog, onTimePicked = {
        viewModel.onEvent(EventDetailsEvent.FromTimeChanged(it))
    })
    DateDialog(dialogState = dateToDialog, onDayPicked = {
        viewModel.onEvent(EventDetailsEvent.ToDateChanged(it))
    })
    TimeDialog(dialogState = timeToDialog, onTimePicked = {
        viewModel.onEvent(EventDetailsEvent.ToTimeChanged(it))
    })

    if (state.addAttendeeDialogIsVisible) {
        AddAttendeeDialog(
            isLoading = state.isLoading,
            onDismissClick = { viewModel.onEvent(EventDetailsEvent.HideAddAttendeeDialog) },
            onAddEmail = { viewModel.onEvent(EventDetailsEvent.AddAttendee) },
            onEmailChanged = { email -> viewModel.onEvent(EventDetailsEvent.OnEmailUpdated(email)) },
            email = state.emailAddress,
            isEmailValid = state.isEmailValid,
            isErrorEmail = state.isErrorEmail,
            emailLabel = state.emailLabel
        )
    }

    if (presenceDialogVisible) {
        DefaultConfirmationDialog(
            onDismissRequest = { presenceDialogVisible = false },
            onYesClick = { viewModel.onEvent(EventDetailsEvent.ChangePresenceState) },
            onNoClick = { presenceDialogVisible = false },
            label = R.string.are_you_sure
        )
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
                    .padding(bottom = 16.dp)
                    .verticalScroll(rememberScrollState())
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
                    onPhotoClick = { onEditPhoto(it.path, state.isEditableForCreator) },
                    onAddPhotoClick = {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    isAddPhotoVisible = state.isAddPhotoVisible,
                    isEditable = state.isEditableForCreator
                )
                Spacer(modifier = Modifier.height(20.dp))
                BaseLine()
                Spacer(modifier = Modifier.height(16.dp))
                DateTimeLineComponent(
                    isEditable = state.isEditableForCreator,
                    localDateTime = state.agendaItem.time,
                    label = R.string.from,
                    onEditDateClick = { dateFromDialog.show() },
                    onEditTimeClick = { timeFromDialog.show() }
                )
                Spacer(modifier = Modifier.height(16.dp))
                BaseLine()
                Spacer(modifier = Modifier.height(16.dp))
                DateTimeLineComponent(
                    isEditable = state.isEditableForCreator,
                    localDateTime = state.agendaItem.to,
                    label = R.string.to,
                    onEditDateClick = { dateToDialog.show() },
                    onEditTimeClick = { timeToDialog.show() }
                )
                Spacer(modifier = Modifier.height(16.dp))
                BaseLine()
                Spacer(modifier = Modifier.height(16.dp))
                ReminderComponent(
                    reminderRange = state.reminderRange,
                    onReminderClick = { reminderRange ->
                        viewModel.onEvent(EventDetailsEvent.ReminderChanged(reminderRange))
                    },
                    isEditable = state.isEditableForAttendee
                )
                Spacer(modifier = Modifier.height(16.dp))
                BaseLine()
                Spacer(modifier = Modifier.height(16.dp))
                VisitorsLabelComponent(
                    onAddVisitorClick = { viewModel.onEvent(EventDetailsEvent.ShowAttendeeDialog) },
                    isEditable = state.isEditableForCreator
                )
                if (state.agendaItem.attendees.isNotEmpty()) {
                    AttendeesComponent(
                        isGoingAttendees = state.isGoingAttendees,
                        isNotGoingAttendees = state.isNotGoingAttendees,
                        isEditable = state.isEditableForCreator,
                        userId = state.agendaItem.host,
                        onDeleteAttendee = { attendee ->
                            viewModel.onEvent(EventDetailsEvent.DeleteAttendee(attendee))
                        }
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                if (state.presenceEvent != null) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = state.presenceEvent.label).uppercase(),
                            style = MaterialTheme.typography.Inter600Size16,
                            color = MaterialTheme.colors.onTextFieldIcon,
                            modifier = Modifier.clickable { presenceDialogVisible = true }
                        )
                    }
                }
            }
        }
    )
}