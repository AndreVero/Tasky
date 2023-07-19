package com.vero.tasky.agenda.presentation.agenda

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vero.tasky.R
import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.model.AgendaItemType
import com.vero.tasky.agenda.presentation.agenda.components.*
import com.vero.tasky.agenda.presentation.components.BaseAgendaScreen
import com.vero.tasky.agenda.presentation.components.DateDialog
import com.vero.tasky.agenda.presentation.components.ProfileIcon
import com.vero.tasky.agenda.presentation.ext.hasNotificationPermission
import com.vero.tasky.core.presentation.components.LocalSnackbarHostState
import com.vero.tasky.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun AgendaScreen(
    viewModel: AgendaViewModel = hiltViewModel(),
    onAgendaItemClick: (agendaItem: AgendaItem, isEditable: Boolean) -> Unit,
    onNewAgendaItemClick: (agendaItemType: AgendaItemType) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = LocalSnackbarHostState.current

    val dialogState = rememberMaterialDialogState()
    val state = viewModel.state

    var isLogOutDropDownVisible by remember {
        mutableStateOf(false)
    }
    var isActionDropDownVisible by remember {
        mutableStateOf(false)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {})

    LaunchedEffect(key1 = true) {
        if (!context.hasNotificationPermission()) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiAgendaEvent.ShowErrorMessage -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = context.getString(event.message),
                        )
                    }
                }
            }
        }
    }

    DateDialog(
        onDayPicked = { date -> viewModel.onEvent(AgendaEvent.OnDayClick(date)) },
        dialogState = dialogState
    )

    BaseAgendaScreen(
        headerContent = {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clickable { dialogState.show() }
                    .testTag("agenda_header"),
                verticalAlignment = Alignment.CenterVertically,

                ) {
                Text(
                    text = state.monthLabel.uppercase(),
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.headerText
                )
                Spacer(modifier = Modifier.width(5.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = stringResource(id = R.string.month_drop_down)
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                ProfileIcon(
                    text = state.userShortName,
                    modifier = Modifier.clickable { isLogOutDropDownVisible = true }
                )
                if (isLogOutDropDownVisible) {
                    DefaultDropDownMenu(
                        actions = hashMapOf(
                            R.string.logout to { viewModel.onEvent(AgendaEvent.LogOut) }
                        ),
                        onDismissRequest = { isLogOutDropDownVisible = false }
                    )
                }
            }
        },
        bodyContent = {
            Column(modifier = Modifier.fillMaxSize()) {
                LazyRow(
                    content = {
                        items(state.days) {
                            DayComponent(
                                day = it,
                                onClick = { date -> viewModel.onEvent(AgendaEvent.OnDayClick(date)) }
                            )
                        }
                    },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Text(
                    text = state.dayLabel,
                    modifier = Modifier.padding(top = 24.dp),
                    color = MaterialTheme.colors.text,
                    style = MaterialTheme.typography.Inter700Size20
                )
                LazyColumn(content = {
                    if (state.currentAgendaItem == null && state.agendaItems.isNotEmpty())
                        item { NeedleComponent() }
                    items(state.agendaItems) { agendaItem ->
                        AgendaComponent(
                            agendaItem = agendaItem,
                            isCurrent = agendaItem == state.currentAgendaItem,
                            onOpenClick = { onAgendaItemClick(agendaItem, false) },
                            onEditClick = { onAgendaItemClick(agendaItem, true) },
                            onDeleteClick = {
                                viewModel.onEvent(AgendaEvent.DeleteAgendaItem(agendaItem))
                            },
                            onCheckChanged = { item ->
                                viewModel.onEvent(AgendaEvent.OnCheckChanged(item))
                            }
                        )
                    }
                })
            }
            FloatingActionButton(
                onClick = { isActionDropDownVisible = true },
                shape = RoundedCornerShape(16.dp),
                backgroundColor = MaterialTheme.colors.buttonBackground,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 40.dp)
            ) {
                if (isActionDropDownVisible) {
                    DefaultDropDownMenu(
                        actions = hashMapOf(
                            R.string.event to { onNewAgendaItemClick(AgendaItemType.EVENT) },
                            R.string.task to { onNewAgendaItemClick(AgendaItemType.TASK) },
                            R.string.reminder to { onNewAgendaItemClick(AgendaItemType.REMINDER) }
                        ),
                        onDismissRequest = { isActionDropDownVisible = false }
                    )
                }
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_new_title),
                    tint = MaterialTheme.colors.buttonText
                )
            }
        }
    )
}