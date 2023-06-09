package com.vero.tasky

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.vero.tasky.core.presentation.components.LocalSnackbarHostState
import com.vero.tasky.core.presentation.navigation.RootNavigation
import com.vero.tasky.ui.theme.TaskyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val state = viewModel.state

        installSplashScreen().apply {
            setKeepOnScreenCondition { state.value.isLoading }
        }

        setContent {
            TaskyTheme {
                val localSnackbarHostState = LocalSnackbarHostState.current
                CompositionLocalProvider(
                    LocalSnackbarHostState provides localSnackbarHostState
                ) {

                    LaunchedEffect(true) {
                        viewModel.uiEvent.collect { errorMessage ->
                            localSnackbarHostState.showSnackbar(getString(errorMessage))
                        }
                    }

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        snackbarHost = {
                            SnackbarHost(hostState = LocalSnackbarHostState.current) { data ->
                                Snackbar(
                                    backgroundColor = Color.Red,
                                    snackbarData = data,
                                    contentColor = Color.White
                                )
                            }
                        },
                    ) { paddingValues ->
                        RootNavigation(
                            navController = rememberNavController(),
                            isLoggedIn = state.collectAsState().value.isLoggedIn,
                            modifier = Modifier.padding(paddingValues)
                        )
                    }
                }
            }
        }
    }
}