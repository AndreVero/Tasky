package com.vero.tasky.core.presentation.navigation

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vero.tasky.auth.presentation.login.LoginScreen

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
            Text(text = "Agenda")
        }
        composable(route = Screens.SignUp.route) {
            Text(text = "SignUp")
        }
        composable(route = Screens.Login.route) {
            LoginScreen(
                onSignUp = { navController.navigate(Screens.SignUp.route) },
                onLogIn = { navController.navigate(Screens.Agenda.route) }
            )
        }
    }
}