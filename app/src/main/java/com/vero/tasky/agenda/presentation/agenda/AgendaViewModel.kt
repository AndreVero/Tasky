package com.vero.tasky.agenda.presentation.agenda

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vero.tasky.agenda.data.util.LocalDateTimeConverter
import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.model.ModificationType
import com.vero.tasky.agenda.domain.usecase.AgendaUseCases
import com.vero.tasky.agenda.presentation.util.LocalDateParser
import com.vero.tasky.agenda.domain.util.UserNameParser
import com.vero.tasky.core.domain.local.UserPreferences
import com.vero.tasky.core.domain.util.eventbus.AuthEventBus
import com.vero.tasky.core.domain.util.eventbus.AuthEventBusEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AgendaViewModel @Inject constructor(
    private val preferences: UserPreferences,
    private val agendaUseCases: AgendaUseCases,
    private val savedStateHandle: SavedStateHandle,
    private val authEventBus: AuthEventBus,
) : ViewModel() {

    var state by mutableStateOf(
        savedStateHandle.get(STATE_KEY)
            ?: getInitialState(LocalDate.now())
    )
        private set

    private val channel = Channel<UiAgendaEvent>()
    val uiEvent = channel.receiveAsFlow()

    private var currentDayJob: Job? = null

    init {
        getAgendaForChosenDay(LocalDate.now())
    }

    fun onEvent(event: AgendaEvent) {
        when (event) {
            is AgendaEvent.DeleteAgendaItem -> {
                deleteAgendaItem(event.agendaItem)
            }
            AgendaEvent.LogOut -> {
                logOut()
            }
            is AgendaEvent.OnDayClick -> {
                getAgendaForChosenDay(event.date)
            }
            is AgendaEvent.OnCheckChanged -> {
                viewModelScope.launch {
                    val task = event.agendaItem
                    agendaUseCases.updateTask(
                        task.copy(isDone = !task.isDone),
                        modificationType = ModificationType.UPDATED
                    )
                }
            }
        }
    }

    private fun deleteAgendaItem(agendaItem: AgendaItem) {
        viewModelScope.launch {
            when (agendaItem) {
                is AgendaItem.Reminder -> agendaUseCases.deleteReminderUseCase(agendaItem)
                is AgendaItem.Event -> agendaUseCases.deleteEventUseCase(agendaItem)
                is AgendaItem.Task -> agendaUseCases.deleteTaskUseCase(agendaItem)
            }
        }
    }

    private fun logOut() {
        viewModelScope.launch {
            authEventBus.sendEvent(AuthEventBusEvent.LogOut)
        }
    }

    private fun updateState(newState: AgendaState) {
        state = newState
        savedStateHandle.set(STATE_KEY, newState)
    }

    private fun getAgendaForChosenDay(day: LocalDate) {
        val from = LocalDateTimeConverter.getEpochForCurrentTimezone(day.atStartOfDay())
        val to = LocalDateTimeConverter.getEpochForCurrentTimezone(
            LocalDateTime.of(day, LocalTime.of(23, 59))
        )

        currentDayJob?.cancel()
        currentDayJob = viewModelScope.launch {
            agendaUseCases.getAgendaForDay(from, to)
                .collectLatest { items ->
                    updateState(
                        state.copy(agendaItems = items)
                    )
                    setCurrentAgendaItem(day, items)
                }
        }

        viewModelScope.launch {
            agendaUseCases.updateAgendaForDay(timestamp = from)
        }

        updateState(getInitialState(date = day))
    }

    private fun getInitialState(date: LocalDate): AgendaState {
        val days = LocalDateParser.getNextDaysFromDate(date)
        return AgendaState(
            userShortName = UserNameParser.toShortName(preferences.getUser()?.fullName ?: ""),
            monthLabel = date.month.getDisplayName(TextStyle.FULL, Locale.getDefault()),
            days = days,
            dayLabel = LocalDateParser.getDayLabel(days[3].date),
        )
    }

    private fun setCurrentAgendaItem(date: LocalDate, agendaItems: List<AgendaItem>) {
        if (date != LocalDate.now())
            updateState(state.copy(currentAgendaItem = null))
        else {
            val currentDayTime = LocalDateTime.now()
            val currentAgendaItem = agendaItems.findLast { it.time <= currentDayTime }
            updateState(state.copy(currentAgendaItem = currentAgendaItem))
        }
    }

    companion object {
        private const val STATE_KEY = "state"
    }
}