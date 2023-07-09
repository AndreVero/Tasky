package com.vero.tasky.agenda.presentation.reminderdetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vero.tasky.agenda.domain.model.ModificationType
import com.vero.tasky.agenda.domain.usecase.reminder.ReminderUseCases
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
class ReminderDetailsViewModel @Inject constructor(
    private val reminderUseCases: ReminderUseCases,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val itemId = savedStateHandle.get<String?>(NavigationConstants.REMINDER_ITEM_ID)
    private val isEditable = savedStateHandle[NavigationConstants.IS_EDITABLE] ?: false

    var state by mutableStateOf(
        (savedStateHandle.get(STATE_KEY) as? ReminderDetailsState)?.copy(
            isLoading = false,
        ) ?: ReminderDetailsState(
            isEditable = isEditable
        ))
        private set

    private val channel = Channel<UiReminderDetailsEvent>()
    val uiEvent = channel.receiveAsFlow()

    init {
        itemId?.let { id ->
            viewModelScope.launch {
                updateState(state.copy(agendaItem = reminderUseCases.getReminder(id),))
            }
        }
    }

    fun onEvent(event: ReminderDetailsEvent) {
        when (event) {
            is ReminderDetailsEvent.AtDateChanged -> changeAtDate(event.date)
            is ReminderDetailsEvent.AtTimeChanged -> changeAtTime(event.time)
            ReminderDetailsEvent.ChangeMode -> changeMode()
            is ReminderDetailsEvent.CheckModifiedInfo -> checkModifiedInfo(event)
            ReminderDetailsEvent.DeleteReminder -> deleteEvent()
            is ReminderDetailsEvent.ReminderChanged -> changeReminder(event.reminderRange)
            ReminderDetailsEvent.SaveReminder -> {
                saveAgendaItem(
                    modificationType = if (itemId != null)
                        ModificationType.UPDATED
                    else ModificationType.CREATED
                )
            }
        }
    }

    private fun checkModifiedInfo(modifiedInfo: ReminderDetailsEvent.CheckModifiedInfo) {
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
            reminderUseCases.saveReminder(
                reminder = state.agendaItem,
                modificationType = modificationType,
            ).onSuccess {
                channel.send(UiReminderDetailsEvent.OnBackClick)
            }
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
            reminderUseCases.deleteReminder(state.agendaItem)
            channel.send(UiReminderDetailsEvent.OnBackClick)
        }
    }

    private fun changeMode() {
        updateState(state.copy(isEditable = !state.isEditable))
    }

    private fun updateState(newState: ReminderDetailsState) {
        state = newState
        savedStateHandle.set(STATE_KEY, newState)
    }

    companion object {
        private const val STATE_KEY = "state"
    }

}