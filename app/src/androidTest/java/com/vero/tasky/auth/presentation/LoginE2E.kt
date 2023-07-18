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
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.compose.rememberNavController
import com.vero.tasky.MainActivity
import com.vero.tasky.auth.data.AuthRepositoryImplFake
import com.vero.tasky.auth.domain.usecase.LoginUseCase
import com.vero.tasky.auth.domain.usecase.LoginUseCases
import com.vero.tasky.auth.domain.usecase.password.ValidatePasswordUseCase
import com.vero.tasky.auth.presentation.login.LoginViewModel
import com.vero.tasky.core.domain.local.UserPreferences
import com.vero.tasky.core.domain.model.User
import com.vero.tasky.core.domain.usecase.ValidateEmailUseCase
import com.vero.tasky.core.domain.util.eventbus.AuthEventBus
import com.vero.tasky.core.presentation.components.LocalSnackbarHostState
import com.vero.tasky.core.presentation.navigation.RootNavigationTest
import com.vero.tasky.ui.theme.TaskyTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class LoginE2E {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var authEventBus: AuthEventBus

    @Inject
    lateinit var validEmailUseCase: ValidateEmailUseCase

    @Inject
    lateinit var preferences: UserPreferences

    private lateinit var loginViewModel: LoginViewModel

    private val validTestEmail = "test@gmail.com"
    private val validTestPassword = "Qwerty12345"

    private lateinit var authRepository: AuthRepositoryImplFake

    @Before
    fun setUp() {
        hiltRule.inject()

        authRepository = AuthRepositoryImplFake()
        loginViewModel = LoginViewModel(
            savedStateHandle = SavedStateHandle(),
            userPreferences = preferences,
            authEventBus = authEventBus,
            loginUseCases = LoginUseCases(
                validateEmailUseCase = validEmailUseCase,
                validatePasswordUseCase = ValidatePasswordUseCase(),
                loginUseCase = LoginUseCase(authRepository)
            )
        )

        preferences.clear()
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
                        RootNavigationTest(
                            navController = rememberNavController(),
                            isLoggedIn = composeRule.activity.viewModel.state.collectAsState().value.isLoggedIn,
                            modifier = Modifier.padding(paddingValues),
                            loginViewModel = loginViewModel
                        )
                    }
                }
            }
        }
    }

    @Test
    fun logIn_success_agendaScreenWasOpen() {
        authRepository.loginResult = Result.success(User("", "", ""))
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
        authRepository.loginResult = Result.failure(Throwable())
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
            .onNodeWithTag("snackbar")
            .assertIsDisplayed()
    }
}