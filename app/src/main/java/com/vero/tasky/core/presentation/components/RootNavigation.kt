package com.vero.tasky.core.presentation.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vero.tasky.core.presentation.navigation.Screens

@Composable
fun RootNavigation(
    navController: NavHostController,
    isLoggedIn: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Screens.Agenda.route else Screens.Login.route
    ) {
        composable(route = Screens.Agenda.route) {
            Text(text = "Agenda")
        }
        composable(route = Screens.Login.route) {
            Text(text = "Login")
        }
    }
}