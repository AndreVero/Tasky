package com.vero.tasky.core.presentation.navigation

import android.content.Intent.ACTION_VIEW
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.vero.tasky.R
import com.vero.tasky.agenda.domain.model.AgendaItem
import com.vero.tasky.agenda.domain.model.AgendaItemType
import com.vero.tasky.agenda.presentation.agenda.AgendaScreen
import com.vero.tasky.agenda.presentation.eventdetails.EventDetailsScreen
import com.vero.tasky.agenda.presentation.editphoto.EditPhotoScreen
import com.vero.tasky.agenda.presentation.edittext.EditTextScreen
import com.vero.tasky.agenda.presentation.reminderdetails.ReminderDetailsScreen
import com.vero.tasky.agenda.presentation.taskdetails.TaskDetailsScreen
import com.vero.tasky.auth.presentation.login.LoginScreen
import com.vero.tasky.auth.presentation.login.LoginViewModel
import com.vero.tasky.auth.presentation.registration.RegistrationScreen
import com.vero.tasky.core.presentation.navigation.NavigationConstants.Companion.EDIT_PHOTO_URI
import com.vero.tasky.core.presentation.navigation.NavigationConstants.Companion.EDIT_TEXT_TYPE
import com.vero.tasky.core.presentation.navigation.NavigationConstants.Companion.EDIT_TEXT_VALUE
import com.vero.tasky.core.presentation.navigation.NavigationConstants.Companion.EVENT_ITEM_ID
import com.vero.tasky.core.presentation.navigation.NavigationConstants.Companion.IS_EDITABLE
import com.vero.tasky.core.presentation.navigation.NavigationConstants.Companion.REMINDER_ITEM_ID
import com.vero.tasky.core.presentation.navigation.NavigationConstants.Companion.TASK_ITEM_ID
import com.vero.tasky.core.presentation.navigation.NavigationConstants.Companion.getAgendaItemScreenArgumentTypes
import com.vero.tasky.core.presentation.navigation.NavigationConstants.Companion.getAgendaItemScreenParameters
import com.vero.tasky.core.presentation.navigation.NavigationConstants.Companion.getEventDeepLink
import com.vero.tasky.core.presentation.navigation.NavigationConstants.Companion.getReminderDeepLink
import com.vero.tasky.core.presentation.navigation.NavigationConstants.Companion.getTaskDeepLink
import com.vero.tasky.ui.theme.Inter400Size18
import com.vero.tasky.ui.theme.Inter400Size26
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun RootNavigationTest(
    navController: NavHostController,
    isLoggedIn: Boolean,
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel? = null
) {

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Screens.Agenda.route else Screens.Login.route,
        modifier = modifier
    ) {
        composable(route = Screens.Agenda.route) {
            AgendaScreen(
                onAgendaItemClick = { agendaItem, isEditable ->
                    val route = when (agendaItem) {
                        is AgendaItem.Event -> Screens.Event.route + "?$EVENT_ITEM_ID"
                        is AgendaItem.Task -> Screens.Task.route + "?$TASK_ITEM_ID"
                        is AgendaItem.Reminder -> Screens.Reminder.route + "?$REMINDER_ITEM_ID"
                    }
                    val parameters = "=${agendaItem.id}&$IS_EDITABLE=$isEditable"
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
            route = Screens.Event.route + getAgendaItemScreenParameters(EVENT_ITEM_ID),
            arguments = getAgendaItemScreenArgumentTypes(EVENT_ITEM_ID),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = getEventDeepLink()
                    action = ACTION_VIEW
                }
            ),
        ) { backEntry ->
            EventDetailsScreen(
                navigateBack = { navController.popBackStack() },
                onEditTitle = { title ->
                    navController.navigate(
                        Screens.EditText.route + "?$EDIT_TEXT_VALUE=$title" +
                                "&$EDIT_TEXT_TYPE=${EditTextScreenType.TITLE}"
                    )
                },
                onEditDescription = { description ->
                    navController.navigate(
                        Screens.EditText.route + "?$EDIT_TEXT_VALUE=$description" +
                                "&$EDIT_TEXT_TYPE=${EditTextScreenType.DESCRIPTION}"
                    )
                },
                onEditPhoto = { uri, isEditable ->
                    val encodedUri = URLEncoder.encode(uri, StandardCharsets.UTF_8.toString())
                    navController.navigate(
                        Screens.EditPhoto.route + "?$EDIT_PHOTO_URI=$encodedUri" +
                                "&$IS_EDITABLE=$isEditable"
                    )
                },
                title = backEntry.savedStateHandle.get(EditTextScreenType.TITLE.toString()),
                description = backEntry.savedStateHandle.get(EditTextScreenType.DESCRIPTION.toString()),
                deletedPhotoUri = backEntry.savedStateHandle.get(EDIT_PHOTO_URI),
            )
        }
        composable(
            route = Screens.EditPhoto.route + "?$EDIT_PHOTO_URI={$EDIT_PHOTO_URI}" +
                    "&$IS_EDITABLE={$IS_EDITABLE}",
            arguments = listOf(
                navArgument(EDIT_PHOTO_URI) {
                    type = NavType.StringType
                },
                navArgument(IS_EDITABLE) {
                    type = NavType.BoolType
                }
            )
        ) {
            val uri = it.arguments?.getString(EDIT_PHOTO_URI) ?: ""
            val isEditable = it.arguments?.getBoolean(IS_EDITABLE) ?: false
            EditPhotoScreen(
                uri = uri,
                label = R.string.edit_photo,
                isEditable = isEditable,
                navBack = { navController.popBackStack() },
                deletePhoto = { resultStr ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(EDIT_PHOTO_URI, resultStr)
                    navController.popBackStack()
                },
            )
        }
        composable(
            route = Screens.EditText.route + "?$EDIT_TEXT_VALUE={$EDIT_TEXT_VALUE}" +
                    "&$EDIT_TEXT_TYPE={$EDIT_TEXT_TYPE}",
            arguments = listOf(
                navArgument(EDIT_TEXT_VALUE) {
                    type = NavType.StringType
                },
                navArgument(EDIT_TEXT_TYPE) {
                    type = NavType.StringType
                }
            )
        ) {
            val text = it.arguments?.getString(EDIT_TEXT_VALUE) ?: ""
            val typeStr = it.arguments?.getString(EDIT_TEXT_TYPE)
            val type = EditTextScreenType.valueOf(
                typeStr
                    ?: EditTextScreenType.DESCRIPTION.toString()
            )
            val label = if (type == EditTextScreenType.TITLE) R.string.edit_title
            else R.string.edit_description
            val textStyle = if (type == EditTextScreenType.TITLE)
                MaterialTheme.typography.Inter400Size26
            else MaterialTheme.typography.Inter400Size18
            EditTextScreen(
                label = label,
                textValue = text,
                navBack = { navController.popBackStack() },
                save = { resultStr ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(type.toString(), resultStr)
                    navController.popBackStack()
                },
                textStyle = textStyle
            )
        }
        composable(
            route = Screens.Task.route +  getAgendaItemScreenParameters(TASK_ITEM_ID),
            arguments = getAgendaItemScreenArgumentTypes(TASK_ITEM_ID),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = getTaskDeepLink()
                    action = ACTION_VIEW
                }
            ),
        ) { backEntry ->
            TaskDetailsScreen(
                navigateBack = { navController.popBackStack() },
                onEditTitle = { title ->
                    navController.navigate(
                        Screens.EditText.route + "?$EDIT_TEXT_VALUE=$title" +
                                "&$EDIT_TEXT_TYPE=${EditTextScreenType.TITLE}"
                    )
                },
                onEditDescription = { description ->
                    navController.navigate(
                        Screens.EditText.route + "?$EDIT_TEXT_VALUE=$description" +
                                "&$EDIT_TEXT_TYPE=${EditTextScreenType.DESCRIPTION}"
                    )
                },
                title = backEntry.savedStateHandle.get(EditTextScreenType.TITLE.toString()),
                description = backEntry.savedStateHandle.get(EditTextScreenType.DESCRIPTION.toString()),
            )
        }
        composable(
            route = Screens.Reminder.route +  getAgendaItemScreenParameters(REMINDER_ITEM_ID),
            arguments = getAgendaItemScreenArgumentTypes(REMINDER_ITEM_ID),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = getReminderDeepLink()
                    action = ACTION_VIEW
                }
            ),
        ) { backEntry ->
            ReminderDetailsScreen(
                navigateBack = { navController.popBackStack() },
                onEditTitle = { title ->
                    navController.navigate(
                        Screens.EditText.route + "?$EDIT_TEXT_VALUE=$title" +
                                "&$EDIT_TEXT_TYPE=${EditTextScreenType.TITLE}"
                    )
                },
                onEditDescription = { description ->
                    navController.navigate(
                        Screens.EditText.route + "?$EDIT_TEXT_VALUE=$description" +
                                "&$EDIT_TEXT_TYPE=${EditTextScreenType.DESCRIPTION}"
                    )
                },
                title = backEntry.savedStateHandle.get(EditTextScreenType.TITLE.toString()),
                description = backEntry.savedStateHandle.get(EditTextScreenType.DESCRIPTION.toString()),
            )
        }
        composable(route = Screens.Registration.route) {
            RegistrationScreen(
                onSignUp = { navController.navigate(Screens.Login.route) },
                onBackPressed = { navController.popBackStack() })
        }
        composable(route = Screens.Login.route) {
            LoginScreen(
                viewModel = loginViewModel ?: hiltViewModel(),
                onSignUp = { navController.navigate(Screens.Registration.route) },
            )
        }
    }
}