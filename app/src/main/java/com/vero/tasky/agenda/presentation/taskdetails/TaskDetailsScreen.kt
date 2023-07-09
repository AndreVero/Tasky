package com.vero.tasky.agenda.presentation.taskdetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vero.tasky.R
import com.vero.tasky.agenda.presentation.agenda.components.DefaultConfirmationDialog
import com.vero.tasky.agenda.presentation.components.*
import com.vero.tasky.agenda.presentation.util.LocalDateParser
import com.vero.tasky.core.presentation.components.LocalSnackbarHostState
import com.vero.tasky.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun TaskDetailsScreen(
    viewModel: TaskDetailsViewModel = hiltViewModel(),
    onEditTitle: (String) -> Unit,
    onEditDescription: (String) -> Unit,
    navigateBack: () -> Unit,
    title: String? = null,
    description: String? = null,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = LocalSnackbarHostState.current

    val dateAtDialog = rememberMaterialDialogState()
    val timeAtDialog = rememberMaterialDialogState()
    var isDeleteDialogVisible by remember { mutableStateOf(false) }
    val state = viewModel.state

    LaunchedEffect(key1 = true) {
        viewModel.onEvent(
            TaskDetailsEvent.CheckModifiedInfo(
                title = title,
                description = description
            )
        )
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiTaskDetailsEvent.ShowErrorMessage -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = context.getString(event.message),
                        )
                    }
                }
                UiTaskDetailsEvent.OnBackClick -> navigateBack()
            }
        }
    }
    DateDialog(dialogState = dateAtDialog, onDayPicked = {
        viewModel.onEvent(TaskDetailsEvent.AtDateChanged(it))
    })
    TimeDialog(dialogState = timeAtDialog, onTimePicked = {
        viewModel.onEvent(TaskDetailsEvent.AtTimeChanged(it))
    })

    if (isDeleteDialogVisible) {
        DefaultConfirmationDialog(
            onDismissRequest = { isDeleteDialogVisible = false },
            onYesClick = { viewModel.onEvent(TaskDetailsEvent.DeleteTask) },
            onNoClick = { isDeleteDialogVisible = false },
            label = R.string.are_you_sure
        )
    }

    BaseAgendaScreen(
        headerContent = {
          AgendaItemHeaderComponent(
              text = LocalDateParser.getDayLabel(state.agendaItem.time.toLocalDate()),
              isLoading = state.isLoading,
              isEditable = state.isEditable,
              onSaveClick = { viewModel.onEvent(TaskDetailsEvent.SaveTask) },
              onChangeModeClick = { viewModel.onEvent(TaskDetailsEvent.ChangeMode) },
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
                    color = taskBackgroundColor,
                    type = R.string.task,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CircleCheckBox(
                        agendaItem = state.agendaItem,
                        backgroundColor = Color.White,
                        onCheckChanged = {}
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    AgendaItemTextComponent(
                        text = state.agendaItem.title,
                        isEditable = state.isEditable,
                        onEditClick = { onEditTitle(state.agendaItem.title) },
                        textStyle = MaterialTheme.typography.Inter700Size26
                    )
                }
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
                        viewModel.onEvent(TaskDetailsEvent.ReminderChanged(reminderRange))
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
                        text = stringResource(id = R.string.delete_task).uppercase(),
                        style = MaterialTheme.typography.Inter600Size16,
                        color = MaterialTheme.colors.onTextFieldIcon,
                        modifier = Modifier.clickable { isDeleteDialogVisible = true }
                    )
                }
            }
        }
    )
}