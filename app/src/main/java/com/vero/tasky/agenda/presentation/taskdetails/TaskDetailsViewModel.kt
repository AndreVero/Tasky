package com.vero.tasky.agenda.presentation.taskdetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vero.tasky.agenda.domain.model.ModificationType
import com.vero.tasky.agenda.domain.usecase.task.TaskUseCases
import com.vero.tasky.agenda.presentation.model.ReminderRange
import com.vero.tasky.agenda.presentation.util.LocalDateParser
import com.vero.tasky.core.presentation.navigation.NavigationConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class TaskDetailsViewModel @Inject constructor(
    private val taskUseCases: TaskUseCases,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val itemId = savedStateHandle.get<String?>(NavigationConstants.TASK_ITEM_ID)
    private val isEditable = savedStateHandle[NavigationConstants.IS_EDITABLE] ?: false

    var state by mutableStateOf(
        (savedStateHandle.get(STATE_KEY) as? TaskDetailsState)?.copy(
            isLoading = false,
        ) ?: TaskDetailsState(
            isEditable = isEditable
        ))
        private set

    private val channel = Channel<UiTaskDetailsEvent>()
    val uiEvent = channel.receiveAsFlow()

    init {
        itemId?.let { id ->
            viewModelScope.launch {
                updateState(state.copy(agendaItem = taskUseCases.getTask(id),))
            }
        }
    }

    fun onEvent(event: TaskDetailsEvent) {
        when (event) {
            is TaskDetailsEvent.AtDateChanged -> changeAtDate(event.date)
            is TaskDetailsEvent.AtTimeChanged -> changeAtTime(event.time)
            TaskDetailsEvent.ChangeMode -> changeMode()
            is TaskDetailsEvent.CheckModifiedInfo -> checkModifiedInfo(event)
            TaskDetailsEvent.DeleteTask -> deleteEvent()
            is TaskDetailsEvent.ReminderChanged -> changeReminder(event.reminderRange)
            TaskDetailsEvent.SaveTask -> {
                saveAgendaItem(
                    modificationType = if (itemId != null)
                        ModificationType.UPDATED
                    else ModificationType.CREATED
                )
            }
        }
    }

    private fun checkModifiedInfo(modifiedInfo: TaskDetailsEvent.CheckModifiedInfo) {
        updateState(
            state.copy(
                agendaItem = state.agendaItem.copy(
                    title = modifiedInfo.title ?: state.agendaItem.title,
                    description = modifiedInfo.description ?: state.agendaItem.description,
                ),
            )
        )
    }

    private fun saveAgendaItem(
        modificationType: ModificationType
    ) {
        viewModelScope.launch {
            taskUseCases.saveTask(
                task = state.agendaItem,
                modificationType = modificationType,
            )
            channel.send(UiTaskDetailsEvent.OnBackClick)
        }
    }

    private fun changeReminder(reminderRange: ReminderRange) {
        updateState(state.copy(reminderRange = reminderRange))
        val agendaItem = state.agendaItem
        val newRemindAt = agendaItem.time.minusMinutes(reminderRange.minutes)
        updateState(
            state.copy(
                agendaItem = agendaItem.copy(
                    remindAt = newRemindAt,
                )
            )
        )
    }

    private fun changeAtDate(date: LocalDate) {
        val newDateTime = LocalDateParser.updateLocalDateTime(state.agendaItem.time, date)
        updateState(state.copy(agendaItem = state.agendaItem.copy(time = newDateTime)))
        changeReminder(state.reminderRange)
    }

    private fun changeAtTime(time: LocalTime) {
        val newDateTime = LocalDateParser.updateLocalDateTime(state.agendaItem.time, time)
        updateState(state.copy(agendaItem = state.agendaItem.copy(time = newDateTime)))
        changeReminder(state.reminderRange)
    }

    private fun deleteEvent() {
        viewModelScope.launch {
            taskUseCases.deleteTask(state.agendaItem)
            channel.send(UiTaskDetailsEvent.OnBackClick)
        }
    }

    private fun changeMode() {
        updateState(state.copy(isEditable = !state.isEditable))
    }

    private fun updateState(newState: TaskDetailsState) {
        state = newState
        savedStateHandle.set(STATE_KEY, newState)
    }

    companion object {
        private const val STATE_KEY = "state"
    }

}