package com.vero.tasky.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vero.tasky.agenda.presentation.agenda.AgendaScreen
import com.vero.tasky.auth.presentation.login.LoginScreen
import com.vero.tasky.auth.presentation.registration.RegistrationScreen

@Composable
fun RootNavigation(
    navController: NavHostController,
    isLoggedIn: Boolean,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Screens.Agenda.route else Screens.Login.route,
        modifier = modifier
    ) {
        composable(route = Screens.Agenda.route) {
            AgendaScreen(
                openEventScreen = { },
                openReminderScreen = { },
                openTaskScreen = { }
            )
        }
        composable(route = Screens.Registration.route) {
            RegistrationScreen(
                onSignUp = { navController.navigate(Screens.Login.route) },
                onBackPressed = { navController.popBackStack() })
        }
        composable(route = Screens.Login.route) {
            LoginScreen(
                onSignUp = { navController.navigate(Screens.Registration.route) },
            )
        }
    }
}