package com.vero.tasky.auth.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vero.tasky.R
import com.vero.tasky.auth.presentation.components.BaseAuthScreen
import com.vero.tasky.auth.presentation.components.PasswordTextField
import com.vero.tasky.core.presentation.ErrorType
import com.vero.tasky.core.presentation.UIEvent
import com.vero.tasky.core.presentation.components.LoadingTextButton
import com.vero.tasky.core.presentation.components.VerifiableTextField
import com.vero.tasky.ui.theme.bottomText
import com.vero.tasky.ui.theme.bottomTextAccent

@Composable
fun LoginScreen(
    navigateTo: (route: String) -> Unit,
    showError: (errorType: ErrorType) -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect {event ->
            when (event) {
                is UIEvent.NavigateTo -> navigateTo(event.route)
                is UIEvent.ShowErrorMessage -> showError(event.error)
            }
        }
    }

    val state = viewModel.state

    BaseAuthScreen(
        titleRes = R.string.login_title,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            VerifiableTextField(
                isValid = state.isEmailValid,
                hintRes = R.string.email_hint,
                text = state.emailAddress,
                onValueChange = { email -> viewModel.onEvent(LoginEvent.OnEmailUpdated(email)) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            PasswordTextField(
                hintRes = R.string.password,
                text = state.password,
                onValueChange = {
                        password -> viewModel.onEvent(LoginEvent.OnPasswordUpdated(password))
                },
                isPasswordVisible = state.isPasswordVisible,
                onPasswordVisibilityUpdate = {
                    viewModel.onEvent(LoginEvent.OnPasswordVisibilityUpdate)
                }
            )
            Spacer(modifier = Modifier.height(25.dp))
            LoadingTextButton(
                textRes = R.string.log_in,
                isLoading = false,
                onClick = { viewModel.onEvent(LoginEvent.LogIn) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Row(modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 40.dp)
        ) {
            Text(
                text = stringResource(id = R.string.dont_have_account).uppercase(),
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.bottomText
            )
            Text(
                text = " ${stringResource(id = R.string.sign_up).uppercase()}",
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.bottomTextAccent,
                modifier = Modifier.clickable {
                    viewModel.onEvent(LoginEvent.SignUp)
                }
            )
        }
    }
}