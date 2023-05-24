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
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.vero.tasky.core.presentation.components.LocalScaffoldState
import com.vero.tasky.core.presentation.navigation.RootNavigation
import com.vero.tasky.ui.theme.TaskyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val state = viewModel.state.value

        installSplashScreen().apply {
            setKeepOnScreenCondition { viewModel.state.value.isLoading }
        }

        setContent {
            TaskyTheme {

                CompositionLocalProvider(
                    LocalScaffoldState provides rememberScaffoldState()
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        scaffoldState = LocalScaffoldState.current,
                        snackbarHost = {
                            SnackbarHost(it) { data ->
                                Snackbar(
                                    backgroundColor = Color.Red,
                                    snackbarData = data,
                                    contentColor = Color.White
                                )
                            }
                        },
                    ) { paddingValues -> RootNavigation(
                        navController = rememberNavController(),
                        isLoggedIn = state.isLoggedIn,
                        modifier = Modifier.padding(paddingValues)
                    )
                    }
                }

            }
        }
    }
}