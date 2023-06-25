package com.vero.tasky.agenda.presentation.agendaevent

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vero.tasky.R
import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.model.AgendaPhoto
import com.vero.tasky.agenda.domain.model.Attendee
import com.vero.tasky.agenda.domain.usecase.event.EventUseCases
import com.vero.tasky.agenda.presentation.agendaevent.model.ReminderRange
import com.vero.tasky.core.domain.local.UserPreferences
import com.vero.tasky.core.presentation.navigation.NavigationConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val eventUseCases: EventUseCases,
    private val savedStateHandle: SavedStateHandle,
    userPreferences: UserPreferences
) : ViewModel() {

    private val userId = userPreferences.getUser()?.userId ?: ""
    private val itemId = savedStateHandle.get<String?>(NavigationConstants.ITEM_ID)

    var state by mutableStateOf(
        (savedStateHandle.get(STATE_KEY) as? EventDetailsState)?.copy(
            isLoading = false
        ) ?: EventDetailsState(
            isEditable = savedStateHandle[NavigationConstants.IS_EDITABLE] ?: false,
            isNewEvent = itemId != null
        )
    )
        private set

    private val channel = Channel<UiEventDetailsEvent>()
    val uiEvent = channel.receiveAsFlow()


    init {
        itemId?.let { id ->
            eventUseCases.getEvent.invoke(id).onEach { agendaItem ->
                updateState(
                    state.copy(
                        agendaItem = agendaItem,
                        isGoing = if (agendaItem.isUserEventCreator) true
                        else agendaItem.attendees.find { userId == it.userId }?.isGoing ?: false,
                    )
                )
            }.launchIn(viewModelScope)
        }
    }

    fun onEvent(event: EventDetailsEvent) {
        when (event) {
            is EventDetailsEvent.AddAttendee -> addAttendee(event.email)
            is EventDetailsEvent.AddPhoto -> addPhoto(event.uri)
            EventDetailsEvent.ChangeIsGoingState -> changeIsGoingState()
            EventDetailsEvent.ChangeMode -> changeMode()
            EventDetailsEvent.CreateEvent -> createEvent()
            EventDetailsEvent.DeleteEvent -> deleteEvent()
            is EventDetailsEvent.FromDateTimeChanged -> changeFromDateTime(event.time)
            is EventDetailsEvent.ReminderChanged -> changeReminder(event.reminderRange)
            is EventDetailsEvent.ToDateTimeChanged -> changeToDateTime(event.time)
            EventDetailsEvent.UpdateEvent -> updateAgendaItemEvent()
            is EventDetailsEvent.DeletePhoto -> deletePhoto(event.key)
        }
    }

    private fun deletePhoto(key: String) {
        val deletedPhotoKeys = mutableListOf<String>()
        deletedPhotoKeys.addAll(state.deletedPhotoKeys)
        deletedPhotoKeys.add(key)
        updateState(state.copy(deletedPhotoKeys = deletedPhotoKeys))
    }

    private fun updateAgendaItemEvent() {
        viewModelScope.launch {
            eventUseCases.updateEvent(
                event = state.agendaItem,
                deletedPhotoKeys = state.deletedPhotoKeys,
                isGoing = state.isGoing
            ).onSuccess {
                channel.send(UiEventDetailsEvent.ShowInfoToast(it.countOfSkippedMessages.toString()))
                channel.send(UiEventDetailsEvent.OnBackClick)
            }.onFailure {
                channel.send(UiEventDetailsEvent.ShowErrorMessage(R.string.default_event_details_error))
            }
        }
    }

    private fun changeIsGoingState() {
        viewModelScope.launch {
            eventUseCases.updateEvent(
                event = state.agendaItem,
                deletedPhotoKeys = state.deletedPhotoKeys,
                isGoing = !state.isGoing
            ).onSuccess {
                channel.send(UiEventDetailsEvent.ShowInfoToast(it.countOfSkippedMessages.toString()))
                channel.send(UiEventDetailsEvent.OnBackClick)
            }.onFailure {
                channel.send(UiEventDetailsEvent.ShowErrorMessage(R.string.default_event_details_error))
            }
        }
    }

    private fun changeReminder(reminderRange: ReminderRange) {
        updateState(state.copy(reminderRange = reminderRange))
        val agendaItem = state.agendaItem
        val newRemindAt = agendaItem.time.minusMinutes(reminderRange.minutes)
        updateEvent(
            agendaItem.copy(
                remindAt = newRemindAt,
            )
        )
    }

    private fun changeFromDateTime(time: LocalDateTime) {
        updateEvent(state.agendaItem.copy(time = time))
        changeReminder(state.reminderRange)
    }

    private fun changeToDateTime(time: LocalDateTime) {
        updateEvent(state.agendaItem.copy(to = time))
    }

    private fun createEvent() {
        viewModelScope.launch {
            eventUseCases.createEvent(state.agendaItem)
                .onSuccess {
                    channel.send(UiEventDetailsEvent.ShowInfoToast(it.countOfSkippedMessages.toString()))
                    channel.send(UiEventDetailsEvent.OnBackClick)
                }
                .onFailure {
                    channel.send(UiEventDetailsEvent.ShowErrorMessage(R.string.default_event_details_error))
                }
            channel.send(UiEventDetailsEvent.OnBackClick)
        }
    }

    private fun deleteEvent() {
        viewModelScope.launch {
            eventUseCases.deleteEvent(state.agendaItem.id)
            channel.send(UiEventDetailsEvent.OnBackClick)
        }
    }

    private fun changeMode() {
        updateState(state.copy(isEditable = !state.isEditable))
    }

    private fun addPhoto(uri: Uri) {
        val photos = mutableListOf<AgendaPhoto>()
        photos.addAll(state.agendaItem.photos)
        photos.add(AgendaPhoto.LocalPhoto(uri.toString()))
        updateEvent(event = state.agendaItem.copy(photos = photos))
    }

    private fun addAttendee(email: String) {
        updateState(state.copy(isLoading = true))
        viewModelScope.launch {
            eventUseCases.checkAttendee(email)
                .onSuccess { attendee ->
                    if (attendee != null) {
                        val attendees = mutableListOf<Attendee>().apply {
                            addAll(state.agendaItem.attendees)
                            add(
                                Attendee(
                                    email = attendee.email,
                                    fullName = attendee.fullName,
                                    userId = attendee.userId,
                                    eventId = state.agendaItem.id,
                                    isGoing = true,
                                    remindAt = 0L
                                )
                            )
                        }
                        updateEvent(event = state.agendaItem.copy(attendees = attendees))
                        updateState(state.copy(isLoading = false))
                    } else {
                        updateState(
                            state.copy(
                                isLoading = false,
                                isErrorEmail = true,
                                emailLabel = R.string.wrong_email
                            )
                        )
                    }
                }
                .onFailure {
                    updateState(
                        state.copy(
                            isLoading = false,
                            isErrorEmail = true,
                            emailLabel = R.string.wrong_email
                        )
                    )
                }
        }
    }

    private fun updateEvent(event: AgendaItem.Event) {
        updateState(state.copy(agendaItem = event))
    }

    private fun updateState(newState: EventDetailsState) {
        state = newState
        savedStateHandle.set(STATE_KEY, newState)
    }

    companion object {
        private const val STATE_KEY = "state"
    }

}