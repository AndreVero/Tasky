package com.vero.tasky.core.presentation.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

class NavigationConstants {

    companion object {
        const val IS_EDITABLE = "isEditable"
        const val EDIT_TEXT_VALUE = "editTextValue"
        const val EDIT_TEXT_TYPE = "editTextType"
        const val EDIT_PHOTO_URI = "editPhotoUri"

        const val DEEP_LINK_HOST = "https://vero.tasky.com/"
        const val EVENT_ITEM_ID = "eventItemId"
        const val REMINDER_ITEM_ID = "reminderItemId"
        const val TASK_ITEM_ID = "taskItemId"

        fun getAgendaItemScreenParameters(idType: String) : String {
            return "?$idType={$idType}&$IS_EDITABLE={$IS_EDITABLE}"
        }

        fun getAgendaItemScreenArgumentTypes(idType: String) : List<NamedNavArgument> {
            val idArgument = navArgument(idType) {
                type = NavType.StringType
                nullable = true
            }
            return listOf(idArgument, navArgument(IS_EDITABLE) {
                type = NavType.BoolType
                defaultValue = true
            })
        }

        fun getEventDeepLink(itemId: String = "{$EVENT_ITEM_ID}") = "$DEEP_LINK_HOST?$EVENT_ITEM_ID=$itemId"
        fun getTaskDeepLink(itemId: String = "{$TASK_ITEM_ID}") = "$DEEP_LINK_HOST?$TASK_ITEM_ID=$itemId"
        fun getReminderDeepLink(itemId: String = "{$REMINDER_ITEM_ID}") = "$DEEP_LINK_HOST?$REMINDER_ITEM_ID=$itemId"
    }

}