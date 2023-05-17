package com.vero.tasky.core.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vero.tasky.core.presentation.navigation.Screens
import com.vero.tasky.splash_feature.presentation.SplashScreen

@Composable
fun RootNavigation(
    navController: NavHostController,
    isLoggedIn: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = Screens.Splash.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = Screens.Splash.route) {
            SplashScreen(
                navigateTo = { route -> navController.navigate(route) },
                isLoggedIn = isLoggedIn
            )
        }
        composable(route = Screens.Agenda.route) {

        }
        composable(route = Screens.Login.route) {

        }
    }
}