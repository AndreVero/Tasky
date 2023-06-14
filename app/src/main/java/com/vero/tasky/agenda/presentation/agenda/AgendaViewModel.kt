package com.vero.tasky.agenda.presentation.agenda

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vero.tasky.agenda.data.util.LocalDateTimeConverter
import com.vero.tasky.agenda.domain.usecase.AgendaUseCases
import com.vero.tasky.agenda.domain.util.LocalDateParser
import com.vero.tasky.agenda.domain.util.UserNameParser
import com.vero.tasky.core.domain.local.UserPreferences
import com.vero.tasky.core.domain.util.eventbus.LogOutEventBus
import com.vero.tasky.core.domain.util.eventbus.LogOutEventBusEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AgendaViewModel @Inject constructor(
    private val preferences: UserPreferences,
    private val agendaUseCases: AgendaUseCases,
    private val savedStateHandle: SavedStateHandle,
    private val logOutEventBus: LogOutEventBus,
) : ViewModel() {

    var state by mutableStateOf(savedStateHandle.get(STATE_KEY)
        ?: getInitialState(LocalDate.now()))
        private set

    private val channel = Channel<UiAgendaEvent>()
    val uiEvent = channel.receiveAsFlow()

    private var currentDayJob: Job? = null

    init { getAgendaForChosenDay(LocalDate.now()) }

    fun onEvent(event: AgendaEvent) {
        when (event) {
            is AgendaEvent.ChangeAgendaItemStatus -> {}
            is AgendaEvent.DeleteAgendaItem -> {}
            AgendaEvent.LogOut -> { logOut() }
            is AgendaEvent.OnDayClick -> { getAgendaForChosenDay(event.date) }
        }
    }

    private fun logOut() {
        viewModelScope.launch {
            logOutEventBus.sendEvent(LogOutEventBusEvent.LogOut)
        }
    }

    private fun updateState(newState: AgendaState) {
        state = newState
        savedStateHandle.set(STATE_KEY, newState)
    }

    private fun getAgendaForChosenDay(day: LocalDate) {
        val timestamp = LocalDateTimeConverter.localDateToLong(day)

        currentDayJob?.cancel()
        currentDayJob = viewModelScope.launch {
            agendaUseCases.getAgendaForDayUseCase(timestamp = timestamp)
                .collectLatest { items ->
                updateState(state.copy(
                    agendaItems = items
                ))
            }
        }

        viewModelScope.launch {
            agendaUseCases.updateAgendaForDayUseCase(timestamp = timestamp)
        }

        updateState(getInitialState(date = day))
    }

    private fun getInitialState(date: LocalDate) : AgendaState {
        val days = LocalDateParser.getNextDaysFromDate(date)
        return AgendaState(
            userShortName = UserNameParser.toShortName(preferences.getUser()?.fullName ?: ""),
            monthLabel = date.month.getDisplayName(TextStyle.FULL, Locale.getDefault()),
            days = days,
            dayLabel = LocalDateParser.getDayLabel(days[0]),
        )
    }

    companion object {
        private const val STATE_KEY = "state"
    }
}