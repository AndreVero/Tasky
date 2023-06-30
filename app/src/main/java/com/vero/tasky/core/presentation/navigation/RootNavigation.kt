package com.vero.tasky.core.presentation.navigation

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vero.tasky.R
import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.model.AgendaItemType
import com.vero.tasky.agenda.presentation.agenda.AgendaScreen
import com.vero.tasky.agenda.presentation.agendaevent.EventDetailsScreen
import com.vero.tasky.agenda.presentation.edittext.EditTextScreen
import com.vero.tasky.auth.presentation.login.LoginScreen
import com.vero.tasky.auth.presentation.registration.RegistrationScreen
import com.vero.tasky.core.presentation.navigation.NavigationConstants.Companion.EDIT_DESCRIPTION_VALUE
import com.vero.tasky.core.presentation.navigation.NavigationConstants.Companion.EDIT_TITLE_VALUE
import com.vero.tasky.core.presentation.navigation.NavigationConstants.Companion.IS_EDITABLE
import com.vero.tasky.core.presentation.navigation.NavigationConstants.Companion.ITEM_ID
import com.vero.tasky.ui.theme.Inter400Size14
import com.vero.tasky.ui.theme.Inter400Size26

@Composable
fun RootNavigation(
    navController: NavHostController,
    isLoggedIn: Boolean,
    modifier: Modifier = Modifier,
) {

    val agendaItemScreenParameters = "?$ITEM_ID={$ITEM_ID}&$IS_EDITABLE={$IS_EDITABLE}"
    val agendaItemArgumentTypes = listOf(
        navArgument(ITEM_ID) {
            type = NavType.StringType
            nullable = true
        },
        navArgument(IS_EDITABLE) {
            type = NavType.BoolType
            defaultValue = true
        }
    )

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Screens.Agenda.route else Screens.Login.route,
        modifier = modifier
    ) {
        composable(route = Screens.Agenda.route) {
            AgendaScreen(
                onAgendaItemClick = { agendaItem, isEditable ->
                    val route = when (agendaItem) {
                        is AgendaItem.Event -> Screens.Event.route
                        is AgendaItem.Reminder -> Screens.Reminder.route
                        is AgendaItem.Task -> Screens.Task.route
                    }
                    val parameters = "?$ITEM_ID=${agendaItem.id}&$IS_EDITABLE=$isEditable"
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
        composable(
            route = Screens.Event.route + agendaItemScreenParameters,
            arguments = agendaItemArgumentTypes
        ) {

            EventDetailsScreen(
                navigateBack = { navController.popBackStack() },
                onEditTitle = { title ->
                    navController.navigate(
                        Screens.EditTitle.route + "?$EDIT_TITLE_VALUE=$title"
                    )
                },
                onEditDescription = { description ->
                    navController.navigate(
                        Screens.EditDescription.route + "?$EDIT_DESCRIPTION_VALUE=$description"
                    )
                },
                title = it.savedStateHandle.get(EDIT_TITLE_VALUE),
                description = it.savedStateHandle.get(EDIT_DESCRIPTION_VALUE),
            )
        }
        composable(
            route = Screens.EditTitle.route + "?$EDIT_TITLE_VALUE={$EDIT_TITLE_VALUE}",
            arguments = listOf(
                navArgument(EDIT_TITLE_VALUE) {
                    type = NavType.StringType
                }
            )
        ) {
            val text = it.arguments?.getString(EDIT_TITLE_VALUE) ?: ""
            EditTextScreen(
                label = R.string.edit_title,
                textValue = text,
                navBack = { navController.popBackStack() },
                save = { resultStr ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(EDIT_TITLE_VALUE, resultStr)
                    navController.popBackStack()
                },
                textStyle = MaterialTheme.typography.Inter400Size26
            )
        }
        composable(
            route = Screens.EditDescription.route + "?$EDIT_DESCRIPTION_VALUE={$EDIT_DESCRIPTION_VALUE}",
            arguments = listOf(
                navArgument(EDIT_DESCRIPTION_VALUE) {
                    type = NavType.StringType
                }
            )
        ) {
            val text = it.arguments?.getString(EDIT_DESCRIPTION_VALUE) ?: ""
            EditTextScreen(
                label = R.string.edit_description,
                textValue = text,
                navBack = { navController.popBackStack() },
                save = { resultStr ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(EDIT_DESCRIPTION_VALUE, resultStr)
                    navController.popBackStack()
                },
                textStyle = MaterialTheme.typography.Inter400Size14
            )
        }
        composable(
            route = Screens.Task.route + agendaItemScreenParameters,
            arguments = agendaItemArgumentTypes
        ) { backStackEntry -> }
        composable(
            route = Screens.Reminder.route + agendaItemScreenParameters,
            arguments = agendaItemArgumentTypes
        ) { backStackEntry -> }
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