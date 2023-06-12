package com.vero.tasky.agenda.presentation.agenda

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.vero.tasky.core.presentation.components.LocalSnackbarHostState
import kotlinx.coroutines.launch

@Composable
fun AgendaScreen(
    viewModel: AgendaViewModel = hiltViewModel(),
    onLogOut: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = LocalSnackbarHostState.current

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
}