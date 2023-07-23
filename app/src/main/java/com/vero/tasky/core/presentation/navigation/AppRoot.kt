package com.vero.tasky.core.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.vero.tasky.MainViewModel
import com.vero.tasky.core.presentation.components.LocalSnackbarHostState

@Composable
fun AppRoot(
    navController: NavHostController
) {
    val context = LocalContext.current
    val viewModel: MainViewModel = hiltViewModel()

    val localSnackbarHostState = LocalSnackbarHostState.current
    CompositionLocalProvider(
        LocalSnackbarHostState provides localSnackbarHostState
    ) {

        LaunchedEffect(true) {
            viewModel.uiEvent.collect { errorMessage ->
                localSnackbarHostState.showSnackbar(context.getString(errorMessage))
            }
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = {
                SnackbarHost(hostState = LocalSnackbarHostState.current) { data ->
                    Snackbar(
                        backgroundColor = Color.Red,
                        snackbarData = data,
                        contentColor = Color.White,
                        modifier = Modifier.testTag("snackbar")
                    )
                }
            },
        ) { paddingValues ->
            RootNavigation(
                navController = navController,
                isLoggedIn = viewModel.state.collectAsState().value.isLoggedIn,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}