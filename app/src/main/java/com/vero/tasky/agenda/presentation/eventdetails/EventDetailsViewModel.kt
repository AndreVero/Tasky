package com.vero.tasky.agenda.presentation.eventdetails

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vero.tasky.R
import com.vero.tasky.agenda.data.util.LocalDateTimeConverter
import com.vero.tasky.agenda.domain.connectivitymanager.ConnectionHandler
import com.vero.tasky.agenda.domain.connectivitymanager.ConnectionStatus
import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.model.AgendaPhoto
import com.vero.tasky.agenda.domain.model.Attendee
import com.vero.tasky.agenda.domain.model.ModificationType
import com.vero.tasky.agenda.domain.usecase.event.EventUseCases
import com.vero.tasky.agenda.domain.util.ReminderRangeParser
import com.vero.tasky.agenda.presentation.eventdetails.model.PresenceEvent
import com.vero.tasky.agenda.presentation.model.ReminderRange
import com.vero.tasky.agenda.presentation.util.LocalDateParser
import com.vero.tasky.core.domain.local.UserPreferences
import com.vero.tasky.core.presentation.navigation.NavigationConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val eventUseCases: EventUseCases,
    private val savedStateHandle: SavedStateHandle,
    private val userPreferences: UserPreferences,
    private val connectionHandler: ConnectionHandler
) : ViewModel() {

    private val itemId = savedStateHandle.get<String?>(NavigationConstants.EVENT_ITEM_ID)
    private val isEditable = savedStateHandle[NavigationConstants.IS_EDITABLE] ?: false
    private val user = userPreferences.getUser()!!

    var state by mutableStateOf(
        (savedStateHandle.get(STATE_KEY) as? EventDetailsState)?.copy(
            isLoading = false,
            isConnected = connectionHandler.isConnected()
        ) ?: EventDetailsState(
            agendaItem = AgendaItem.Event(
                id = UUID.randomUUID().toString(),
                title = "New event",
                description = "New event description",
                time = LocalDateTime.now().plusMinutes(30),
                to = LocalDateTime.now().plusMinutes(40),
                remindAt = LocalDateTime.now().plusMinutes(20),
                isUserEventCreator = true,
                attendees = emptyList(),
                photos = emptyList(),
                host = user.userId
            ),
            isConnected = connectionHandler.isConnected(),
            isEditableForCreator = isEditable && itemId == null,
            isEditableForAttendee = isEditable,
            presenceEvent = if (itemId != null) PresenceEvent.DELETE else null
        )
    )
        private set

    private val channel = Channel<UiEventDetailsEvent>()
    val uiEvent = channel.receiveAsFlow()

    init {
        itemId?.let { id ->
            eventUseCases.getEvent.invoke(id).onEach { agendaItem ->
                ReminderRangeParser.getRangeFromDateTime(
                    time = agendaItem.time, reminder = agendaItem.remindAt
                )
                updateState(
                    state.copy(
                        agendaItem = agendaItem,
                        isGoing = if (agendaItem.isUserEventCreator) true
                        else agendaItem.attendees.find { user.userId == it.userId }?.isGoing
                            ?: false,
                        isEditableForCreator = agendaItem.isUserEventCreator && isEditable,
                        isEditableForAttendee = isEditable,
                        presenceEvent = getCurrentPresenceEvent(agendaItem),
                        isAddPhotoVisible = isPhotosSizeExceedsLimit(agendaItem),
                        reminderRange = ReminderRangeParser.getRangeFromDateTime(
                            agendaItem.time, agendaItem.remindAt
                        )
                    )
                )
                filterAttendees(agendaItem.attendees)
            }.launchIn(viewModelScope)
        }

        viewModelScope.launch {
            connectionHandler.observeConnectionState().collectLatest {
                val isConnected = it == ConnectionStatus.Available
                updateState(state.copy(isConnected = isConnected))
            }
        }
    }

    fun onEvent(event: EventDetailsEvent) {
        when (event) {
            EventDetailsEvent.AddAttendee -> addAttendee()
            is EventDetailsEvent.AddPhoto -> addPhoto(event.uri)
            EventDetailsEvent.ChangeMode -> changeMode()
            EventDetailsEvent.ChangePresenceState -> changePresenceState()
            is EventDetailsEvent.ReminderChanged -> changeReminder(event.reminderRange)
            EventDetailsEvent.SaveEvent -> saveAgendaItem(
                isGoing = state.isGoing,
                modificationType = if (itemId != null)
                    ModificationType.UPDATED
                else ModificationType.CREATED
            )
            is EventDetailsEvent.DeletePhoto -> deletePhoto(event.key)
            is EventDetailsEvent.CheckModifiedInfo -> checkModifiedInfo(event)
            is EventDetailsEvent.FromDateChanged -> changeFromDate(event.date)
            is EventDetailsEvent.FromTimeChanged -> changeFromTime(event.time)
            is EventDetailsEvent.ToDateChanged -> changeToDate(event.date)
            is EventDetailsEvent.ToTimeChanged -> changeToTime(event.time)
            is EventDetailsEvent.OnEmailUpdated -> {
                val email = event.email
                updateState(
                    state.copy(
                        emailAddress = email,
                        isEmailValid = eventUseCases.validateEmailUseCase(email),
                        isErrorEmail = false,
                        emailLabel = R.string.email_hint
                    )
                )
            }
            is EventDetailsEvent.DeleteAttendee -> {
                val attendees = state.agendaItem.attendees - event.attendee
                updateState(
                    state.copy(
                        agendaItem = state.agendaItem.copy(
                            attendees = attendees
                        )
                    )
                )
                filterAttendees(attendees)
            }
            EventDetailsEvent.HideAddAttendeeDialog -> {
                updateState(state.copy(addAttendeeDialogIsVisible = false))
            }
            EventDetailsEvent.ShowAttendeeDialog -> {
                updateState(state.copy(addAttendeeDialogIsVisible = true))
            }
        }
    }

    private fun changePresenceState() {
        when (state.presenceEvent) {
            PresenceEvent.DELETE -> {
                deleteEvent()
            }
            PresenceEvent.LEAVE -> {
                val attendee = getCurrentUserAsAttendee() ?: return
                changeAttendeeState(attendee, attendee.copy(isGoing = false))
                saveAgendaItem(modificationType = ModificationType.UPDATED, isGoing = false)
            }
            PresenceEvent.JOIN -> {
                val attendee = getCurrentUserAsAttendee() ?: return
                changeAttendeeState(attendee, attendee.copy(isGoing = true))
                saveAgendaItem(modificationType = ModificationType.UPDATED, isGoing = true)
            }
            else -> return
        }
    }

    private fun checkModifiedInfo(modifiedInfo: EventDetailsEvent.CheckModifiedInfo) {
        updateState(
            state.copy(
                agendaItem = state.agendaItem.copy(
                    title = modifiedInfo.title ?: state.agendaItem.title,
                    description = modifiedInfo.description ?: state.agendaItem.description,
                ),
            )
        )

        modifiedInfo.deletedPhotoUri?.let { deletedPhotoUri ->
            val deletedPhoto = state.agendaItem.photos.find { it.path == deletedPhotoUri } ?: return
            when (deletedPhoto) {
                is AgendaPhoto.LocalPhoto -> {
                    updateState(
                        state.copy(
                            agendaItem = state.agendaItem.copy(
                                photos = state.agendaItem.photos - deletedPhoto
                            )
                        )
                    )
                }
                is AgendaPhoto.RemotePhoto -> {
                    updateState(
                        state.copy(
                            agendaItem = state.agendaItem.copy(
                                photos = state.agendaItem.photos - deletedPhoto
                            ),
                            deletedPhotoKeys = state.deletedPhotoKeys + deletedPhoto.key
                        )
                    )
                }
            }
        }
    }

    private fun deletePhoto(key: String) {
        updateState(state.copy(deletedPhotoKeys = state.deletedPhotoKeys + key))
    }

    private fun saveAgendaItem(
        isGoing: Boolean,
        modificationType: ModificationType
    ) {
        viewModelScope.launch {
            eventUseCases.saveEvent(
                event = state.agendaItem,
                deletedPhotoKeys = state.deletedPhotoKeys,
                modificationType = modificationType,
                currentUser = getCurrentUserAsAttendee(modificationType),
                isGoing = isGoing
            ).onSuccess {
                if (it.countOfSkippedMessages > 0)
                    channel.send(UiEventDetailsEvent.ShowInfoToast(it.countOfSkippedMessages.toString()))
                channel.send(UiEventDetailsEvent.OnBackClick)
            }
        }
    }

    private fun getCurrentUserAsAttendee(modificationType: ModificationType) =
        if (modificationType == ModificationType.CREATED) {
            Attendee(
                email = userPreferences.getEmail(),
                fullName = user.fullName,
                userId = user.userId,
                eventId = state.agendaItem.id,
                isGoing = true,
                remindAt = LocalDateTimeConverter.getEpochForUTC(state.agendaItem.remindAt)
            )
        } else null

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
        if (!agendaItem.isUserEventCreator) {
            val attendee = getCurrentUserAsAttendee() ?: return
            changeAttendeeState(attendee, attendee.copy(
                remindAt = LocalDateTimeConverter.getEpochForUTC(newRemindAt))
            )
        }
    }

    private fun getCurrentUserAsAttendee() : Attendee?
        = state.agendaItem.attendees.find { it.userId == user.userId }

    private fun changeAttendeeState(oldAttendee: Attendee, newAttendeeState: Attendee) {
        updateState(
            state.copy(
                agendaItem = state.agendaItem.copy(
                    attendees = state.agendaItem.attendees - oldAttendee + newAttendeeState
                )
            )
        )
    }

    private fun changeFromDate(date: LocalDate) {
        val newDateTime = LocalDateParser.updateLocalDateTime(state.agendaItem.time, date)
        updateState(state.copy(agendaItem = state.agendaItem.copy(time = newDateTime)))
        changeReminder(state.reminderRange)
    }

    private fun changeToDate(date: LocalDate) {
        val newDateTime = LocalDateParser.updateLocalDateTime(state.agendaItem.to, date)
        updateState(state.copy(agendaItem = state.agendaItem.copy(to = newDateTime)))
    }

    private fun changeFromTime(time: LocalTime) {
        val newDateTime = LocalDateParser.updateLocalDateTime(state.agendaItem.time, time)
        updateState(state.copy(agendaItem = state.agendaItem.copy(time = newDateTime)))
        changeReminder(state.reminderRange)
    }

    private fun changeToTime(time: LocalTime) {
        val newDateTime = LocalDateParser.updateLocalDateTime(state.agendaItem.to, time)
        updateState(state.copy(agendaItem = state.agendaItem.copy(to = newDateTime)))
    }

    private fun deleteEvent() {
        viewModelScope.launch {
            eventUseCases.deleteEvent(state.agendaItem)
            channel.send(UiEventDetailsEvent.OnBackClick)
        }
    }

    private fun changeMode() {
        updateState(
            state.copy(
                isEditableForAttendee = !state.isEditableForAttendee,
                isEditableForCreator = if (state.agendaItem.host == user.userId) !state.isEditableForCreator
                else false
            )
        )
    }

    private fun addPhoto(uri: Uri) {
        val agendaItem = state.agendaItem
        val newPhoto = AgendaPhoto.LocalPhoto(uri.toString())

        updateState(
            state.copy(
                agendaItem = agendaItem.copy(
                    photos = agendaItem.photos + newPhoto
                ),
                isAddPhotoVisible = isPhotosSizeExceedsLimit(agendaItem)
            )
        )
    }

    private fun addAttendee() {
        updateState(state.copy(isLoading = true))
        viewModelScope.launch {
            eventUseCases.checkAttendee(state.emailAddress)
                .onSuccess { attendee ->
                    if (attendee != null) {
                        val newAttendee = Attendee(
                            email = attendee.email,
                            fullName = attendee.fullName,
                            userId = attendee.userId,
                            eventId = state.agendaItem.id,
                            isGoing = true,
                            remindAt = LocalDateTimeConverter.getEpochForUTC(
                                state.agendaItem.remindAt
                            ),
                        )
                        val agendaItem = state.agendaItem
                        val attendees = agendaItem.attendees + newAttendee
                        updateState(
                            state.copy(
                                agendaItem = agendaItem.copy(
                                    attendees = attendees
                                ),
                                addAttendeeDialogIsVisible = false,
                                isLoading = false
                            )
                        )
                        filterAttendees(attendees)
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

    private fun isPhotosSizeExceedsLimit(agendaItem: AgendaItem.Event) = agendaItem.photos.size < 10


    private fun getCurrentPresenceEvent(agendaItem: AgendaItem.Event) =
        if (agendaItem.isUserEventCreator) PresenceEvent.DELETE
        else if (agendaItem.attendees.find { user.userId == it.userId }?.isGoing == true)
            PresenceEvent.LEAVE
        else PresenceEvent.JOIN

    private fun filterAttendees(attendees: List<Attendee>) {
        updateState(
            state.copy(
                isNotGoingAttendees = attendees.filter { !it.isGoing },
                isGoingAttendees = attendees.filter { it.isGoing })
        )
    }

    private fun updateState(newState: EventDetailsState) {
        state = newState
        savedStateHandle.set(STATE_KEY, newState)
    }

    companion object {
        private const val STATE_KEY = "state"
    }

}