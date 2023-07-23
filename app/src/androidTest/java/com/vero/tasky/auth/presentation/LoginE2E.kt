package com.vero.tasky.auth.presentation

import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.vero.tasky.MainActivity
import com.vero.tasky.auth.data.remote.AuthTestConstants.errorTestEmail
import com.vero.tasky.auth.data.remote.AuthTestConstants.validTestEmail
import com.vero.tasky.auth.data.remote.AuthTestConstants.validTestPassword
import com.vero.tasky.core.presentation.navigation.AppRoot
import com.vero.tasky.ui.theme.TaskyTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
@HiltAndroidTest
class LoginE2E {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        composeRule.activity.setContent {
            TaskyTheme {
                AppRoot()
            }
        }
    }


    @Test
    fun logIn_success_agendaScreenWasOpen() {
        composeRule.waitUntilAtLeastOneExists(hasTestTag("email_text_field"))
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
        composeRule.waitUntilAtLeastOneExists(hasTestTag("email_text_field"))
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