package com.vero.tasky.agenda.presentation.agenda

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vero.tasky.agenda.presentation.agenda.components.*
import com.vero.tasky.agenda.presentation.components.BaseAgendaScreen
import com.vero.tasky.core.presentation.components.LocalSnackbarHostState
import com.vero.tasky.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun AgendaScreen(
    viewModel: AgendaViewModel = hiltViewModel(),
    onLogOut: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = LocalSnackbarHostState.current


    val dialogState = rememberMaterialDialogState()
    val state = viewModel.state

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                UiAgendaEvent.OnLogOut -> onLogOut()
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

    CalendarDialog(
        onDayPicked = { date -> viewModel.onEvent(AgendaEvent.OnDayClick(date)) },
        dialogState = dialogState
    )

    BaseAgendaScreen(
        headerContent = {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clickable { dialogState.show() },
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
                    contentDescription = "Month drop-down"
                )
            }
            ProfileIcon(
                text = state.userShortName,
                color = MaterialTheme.colors.profileIcon,
                modifier = Modifier.align(
                    Alignment.CenterEnd
                )
            )
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
                            onOpenClick = {},
                            onEditClick = {},
                            onDeleteClick = {},
                            onCheckChanged = {}
                        )
                    }
                })
            }
            FloatingActionButton(
                onClick = { viewModel.onEvent(AgendaEvent.OnNewItemClick) },
                shape = RoundedCornerShape(16.dp),
                backgroundColor = MaterialTheme.colors.buttonBackground,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add new item",
                    tint = MaterialTheme.colors.buttonText
                )
            }
        }
    )
}