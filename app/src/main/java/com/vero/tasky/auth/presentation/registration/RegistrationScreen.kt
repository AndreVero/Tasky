package com.vero.tasky.auth.presentation.registration

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vero.tasky.R
import com.vero.tasky.auth.presentation.components.BaseAuthScreen
import com.vero.tasky.auth.presentation.components.PasswordTextField
import com.vero.tasky.core.presentation.components.LoadingTextButton
import com.vero.tasky.core.presentation.components.LocalSnackbarHostState
import com.vero.tasky.core.presentation.components.VerifiableTextField
import com.vero.tasky.ui.theme.buttonBackground
import com.vero.tasky.ui.theme.buttonText
import kotlinx.coroutines.launch

@Composable
fun RegistrationScreen(
    onSignUp: () -> Unit,
    onBackPressed: () -> Unit,
    viewModel : RegistrationViewModel = hiltViewModel()
) {

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = LocalSnackbarHostState.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect {event ->
            when (event) {
                UiRegistrationEvent.OnSignUp -> onSignUp()
                is UiRegistrationEvent.ShowErrorMessage -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = context.getString(event.message),
                        )
                    }
                }
            }
        }
    }

    val state = viewModel.state
    
    BaseAuthScreen(titleRes = R.string.sign_up_title) {
        Column {
            VerifiableTextField(
                isValid = state.isNameValid,
                label = state.nameLabel,
                text = state.name,
                onValueChange = {
                        email -> viewModel.onEvent(RegistrationEvent.OnNameUpdated(email))
                },
                isError = state.isErrorName
            )
            Spacer(modifier = Modifier.height(16.dp))
            VerifiableTextField(
                isValid = state.isEmailValid,
                label = state.emailLabel,
                text = state.emailAddress,
                onValueChange = {
                        email -> viewModel.onEvent(RegistrationEvent.OnEmailUpdated(email))
                },
                isError = state.isErrorEmail
            )
            Spacer(modifier = Modifier.height(16.dp))
            PasswordTextField(
                label = state.passwordLabel,
                text = state.password,
                onValueChange = {
                        password -> viewModel.onEvent(RegistrationEvent.OnPasswordUpdated(password))
                },
                isError = state.isErrorPassword
            )
            Spacer(modifier = Modifier.height(25.dp))
            LoadingTextButton(
                textRes = R.string.sign_up,
                isLoading = state.isLoading,
                onClick = { viewModel.onEvent(RegistrationEvent.SignUp) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        FloatingActionButton(
            onClick = onBackPressed,
            shape = RoundedCornerShape(16.dp),
            backgroundColor = MaterialTheme.colors.buttonBackground,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = stringResource(id = R.string.back),
                tint = MaterialTheme.colors.buttonText
            )
        }
    }
}