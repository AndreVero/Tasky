package com.vero.tasky.agenda.presentation.reminderdetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vero.tasky.R
import com.vero.tasky.agenda.presentation.agenda.components.DefaultConfirmationDialog
import com.vero.tasky.agenda.presentation.components.*
import com.vero.tasky.agenda.presentation.util.LocalDateParser
import com.vero.tasky.ui.theme.*
import java.util.*

@Composable
fun ReminderDetailsScreen(
    viewModel: ReminderDetailsViewModel = hiltViewModel(),
    onEditTitle: (String) -> Unit,
    onEditDescription: (String) -> Unit,
    navigateBack: () -> Unit,
    title: String? = null,
    description: String? = null,
) {
    val dateAtDialog = rememberMaterialDialogState()
    val timeAtDialog = rememberMaterialDialogState()
    var isDeleteDialogVisible by remember { mutableStateOf(false) }
    val state = viewModel.state

    LaunchedEffect(key1 = true) {
        viewModel.onEvent(
            ReminderDetailsEvent.CheckModifiedInfo(
                title = title,
                description = description
            )
        )
        viewModel.uiEvent.collect { event ->
            when (event) {
                UiReminderDetailsEvent.OnBackClick -> navigateBack()
            }
        }
    }
    DateDialog(dialogState = dateAtDialog, onDayPicked = {
        viewModel.onEvent(ReminderDetailsEvent.AtDateChanged(it))
    })
    TimeDialog(dialogState = timeAtDialog, onTimePicked = {
        viewModel.onEvent(ReminderDetailsEvent.AtTimeChanged(it))
    })

    if (isDeleteDialogVisible) {
        DefaultConfirmationDialog(
            onDismissRequest = { isDeleteDialogVisible = false },
            onYesClick = { viewModel.onEvent(ReminderDetailsEvent.DeleteReminder) },
            onNoClick = { isDeleteDialogVisible = false },
            label = R.string.are_you_sure
        )
    }

    BaseAgendaScreen(
        headerContent = {
            AgendaItemHeaderComponent(
                text = LocalDateParser.getDayLabel(state.agendaItem.time.toLocalDate(), Locale.getDefault()),
                isLoading = state.isLoading,
                isEditable = state.isEditable,
                onSaveClick = { viewModel.onEvent(ReminderDetailsEvent.SaveReminder) },
                onChangeModeClick = { viewModel.onEvent(ReminderDetailsEvent.ChangeMode) },
                onBackClick = navigateBack,
            )
        },
        bodyContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                AgendaItemTypeComponent(
                    color = reminderBackgroundColor,
                    type = R.string.reminder,
                )
                Spacer(modifier = Modifier.height(16.dp))
                AgendaItemTextComponent(
                    text = state.agendaItem.title,
                    isEditable = state.isEditable,
                    onEditClick = { onEditTitle(state.agendaItem.title) },
                    textStyle = MaterialTheme.typography.Inter700Size26
                )
                Spacer(modifier = Modifier.height(16.dp))
                BaseLine()
                Spacer(modifier = Modifier.height(16.dp))
                AgendaItemTextComponent(
                    text = state.agendaItem.description ?: "",
                    isEditable = state.isEditable,
                    onEditClick = { onEditDescription(state.agendaItem.description ?: "") },
                    textStyle = MaterialTheme.typography.Inter400Size16
                )
                Spacer(modifier = Modifier.height(20.dp))
                BaseLine()
                Spacer(modifier = Modifier.height(16.dp))
                DateTimeLineComponent(
                    isEditable = state.isEditable,
                    localDateTime = state.agendaItem.time,
                    label = R.string.at,
                    onEditDateClick = { dateAtDialog.show() },
                    onEditTimeClick = { timeAtDialog.show() }
                )
                Spacer(modifier = Modifier.height(16.dp))
                BaseLine()
                Spacer(modifier = Modifier.height(16.dp))
                ReminderComponent(
                    reminderRange = state.reminderRange,
                    onReminderClick = { reminderRange ->
                        viewModel.onEvent(ReminderDetailsEvent.ReminderChanged(reminderRange))
                    },
                    isEditable = state.isEditable
                )
                Spacer(modifier = Modifier.height(16.dp))
                BaseLine()
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.delete_reminder).uppercase(),
                        style = MaterialTheme.typography.Inter600Size16,
                        color = MaterialTheme.colors.onTextFieldIcon,
                        modifier = Modifier.clickable { isDeleteDialogVisible = true }
                    )
                }
            }
        }
    )
}