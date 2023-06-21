package com.vero.tasky.core.presentation.navigation

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.model.AgendaItemType
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
                openAgendaItemScreen = { agendaItem, isEditable ->
                    val route = when(agendaItem) {
                        is AgendaItem.Event -> Screens.Event.route
                        is AgendaItem.Reminder -> Screens.Reminder.route
                        is AgendaItem.Task -> Screens.Task.route
                    }
                    val parameters = "?itemId=${agendaItem.id}&isEditable=$isEditable"
                    navController.navigate("$route$parameters")
                },
                openNewAgendaItemScreen = { agendaItemType ->
                    val route = when (agendaItemType) {
                        AgendaItemType.REMINDER -> Screens.Reminder.route
                        AgendaItemType.TASK -> Screens.Task.route
                        else -> Screens.Event.route
                    }
                    navController.navigate(route)
                }
            )
        }
        composable(
            route = Screens.Event.route + "?itemId={itemId}&isEditable={isEditable}",
            arguments = listOf(
                navArgument("itemId") {
                    type = NavType.StringType
                    nullable = true
                                      },
                navArgument("isEditable") {
                    type = NavType.BoolType
                    defaultValue = true
                }
            )
        ) { backStackEntry ->
            Text("Event :" +
                    " ${backStackEntry.arguments?.getString("itemId")} " +
                    "isEditable :  ${backStackEntry.arguments?.getBoolean("isEditable")}")
        }
        composable(
            route = Screens.Task.route + "?itemId={itemId}&isEditable={isEditable}",
            arguments = listOf(
                navArgument("itemId") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("isEditable") {
                    type = NavType.BoolType
                    defaultValue = true
                }
            )
        ) { backStackEntry ->
            Text("Task :" +
                    " ${backStackEntry.arguments?.getString("itemId")} " +
                    "isEditable :  ${backStackEntry.arguments?.getBoolean("isEditable")}")
        }
        composable(
            route = Screens.Reminder.route + "?itemId={itemId}&isEditable={isEditable}",
            arguments = listOf(
                navArgument("itemId") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("isEditable") {
                    type = NavType.BoolType
                    defaultValue = true
                }
            )
        ) { backStackEntry ->
            Text("Reminder :" +
                    " ${backStackEntry.arguments?.getString("itemId")} " +
                    "isEditable :  ${backStackEntry.arguments?.getBoolean("isEditable")}")
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