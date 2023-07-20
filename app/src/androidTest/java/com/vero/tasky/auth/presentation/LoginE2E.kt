package com.vero.tasky.auth.presentation

import androidx.activity.compose.setContent
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vero.tasky.MainActivity
import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.model.AgendaItemType
import com.vero.tasky.agenda.presentation.agenda.AgendaScreen
import com.vero.tasky.auth.data.remote.AuthTestConstants.errorTestEmail
import com.vero.tasky.auth.data.remote.AuthTestConstants.validTestEmail
import com.vero.tasky.auth.data.remote.AuthTestConstants.validTestPassword
import com.vero.tasky.auth.presentation.login.LoginScreen
import com.vero.tasky.core.presentation.components.LocalSnackbarHostState
import com.vero.tasky.core.presentation.navigation.NavigationConstants
import com.vero.tasky.core.presentation.navigation.Screens
import com.vero.tasky.ui.theme.TaskyTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class LoginE2E {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()

        composeRule.activity.setContent {
            TaskyTheme {
                val localSnackbarHostState = LocalSnackbarHostState.current
                CompositionLocalProvider(
                    LocalSnackbarHostState provides localSnackbarHostState
                ) {

                    LaunchedEffect(true) {
                        composeRule.activity.viewModel.uiEvent.collect { errorMessage ->
                            localSnackbarHostState.showSnackbar(composeRule.activity.getString(errorMessage))
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
                        val navController = rememberNavController()
                        NavHost(
                            navController = navController,
                            startDestination = if (
                                composeRule.activity.viewModel.state.collectAsState().value.isLoggedIn
                            ) Screens.Agenda.route else Screens.Login.route,
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            composable(route = Screens.Login.route) {
                                LoginScreen(
                                    onSignUp = { navController.navigate(Screens.Registration.route) },
                                )
                            }
                            composable(route = Screens.Agenda.route) {
                                AgendaScreen(
                                    onAgendaItemClick = { agendaItem, isEditable ->
                                        val route = when (agendaItem) {
                                            is AgendaItem.Event -> Screens.Event.route + "?${NavigationConstants.EVENT_ITEM_ID}"
                                            is AgendaItem.Task -> Screens.Task.route + "?${NavigationConstants.TASK_ITEM_ID}"
                                            is AgendaItem.Reminder -> Screens.Reminder.route + "?${NavigationConstants.REMINDER_ITEM_ID}"
                                        }
                                        val parameters =
                                            "=${agendaItem.id}&${NavigationConstants.IS_EDITABLE}=$isEditable"
                                        navController.navigate("$route$parameters")
                                    },
                                    onNewAgendaItemClick = { agendaItemType ->
                                        val route = when (agendaItemType) {
                                            AgendaItemType.REMINDER -> Screens.Reminder.route
                                            AgendaItemType.TASK -> Screens.Task.route
                                            else -> Screens.Event.route
                                        }
                                        navController.navigate(route)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun logIn_success_agendaScreenWasOpen() {
        composeRule
            .onNodeWithTag("email_text_field")
            .performTextInput(validTestEmail)
        composeRule
            .onNodeWithTag("password_text_field")
            .performTextInput(validTestPassword)
        composeRule
            .onNodeWithText("LOG IN")
            .performClick()
        composeRule
            .onNodeWithTag("agenda_header")
            .assertExists()
    }

    @Test
    fun logIn_failure_errorWasShown() {
        composeRule
            .onNodeWithTag("email_text_field")
            .performTextInput(errorTestEmail)
        composeRule
            .onNodeWithTag("password_text_field")
            .performTextInput(validTestPassword)
        composeRule
            .onNodeWithText("LOG IN")
            .performClick()
        composeRule
            .onNodeWithTag("snackbar")
            .assertIsDisplayed()
    }
}