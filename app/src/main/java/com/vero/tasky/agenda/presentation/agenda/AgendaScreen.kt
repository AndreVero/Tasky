package com.vero.tasky.agenda.presentation.agenda

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vero.tasky.agenda.presentation.agenda.components.DayComponent
import com.vero.tasky.agenda.presentation.agenda.components.ProfileIcon
import com.vero.tasky.agenda.presentation.components.BaseAgendaScreen
import com.vero.tasky.core.presentation.components.LocalSnackbarHostState
import com.vero.tasky.ui.theme.headerText
import com.vero.tasky.ui.theme.profileIcon
import com.vero.tasky.ui.theme.text
import kotlinx.coroutines.launch

@Composable
fun AgendaScreen(
    viewModel: AgendaViewModel = hiltViewModel(),
    onLogOut: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = LocalSnackbarHostState.current

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

    BaseAgendaScreen(
        headerContent = {
            Row(modifier = Modifier.align(Alignment.CenterStart)) {
                Text(
                    text = state.monthLabel,
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.headerText
                )
                Spacer(modifier = Modifier.width(5.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Month drop-down")
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
                            DayComponent(day = it)
                        }
                    },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp)
                )
                Text(
                    text = state.monthLabel,
                    modifier = Modifier.padding(22.dp),
                    color = MaterialTheme.colors.text,
                    style = MaterialTheme.typography.h3
                )
            }
        }
    )
}