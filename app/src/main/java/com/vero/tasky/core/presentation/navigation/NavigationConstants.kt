package com.vero.tasky.core.presentation.navigation

class NavigationConstants {

    companion object {
        const val ITEM_ID = "itemId"
        const val IS_EDITABLE = "isEditable"
        const val EDIT_TEXT_VALUE = "editTextValue"
        const val EDIT_TEXT_TYPE = "editTextType"
        const val EDIT_PHOTO_URI = "editPhotoUri"

        const val DEEP_LINK_HOST = "https://vero.tasky.com/"
        const val EVENT_ITEM_ID = "eventItemId"
        const val REMINDER_ITEM_ID = "reminderItemId"
        const val TASK_ITEM_ID = "taskItemId"

        private const val EVENT_DEEP_LINK = "$DEEP_LINK_HOST?$EVENT_ITEM_ID="
        private const val TASK_DEEP_LINK = "$DEEP_LINK_HOST?$TASK_ITEM_ID="
        private const val REMINDER_DEEP_LINK = "$DEEP_LINK_HOST?$REMINDER_ITEM_ID="

        fun getEventDeepLink(itemId: String = EVENT_ITEM_ID) = "$EVENT_DEEP_LINK{$itemId}"
        fun getTaskDeepLink(itemId: String = TASK_ITEM_ID) = "$TASK_DEEP_LINK{$itemId}"
        fun getReminderDeepLink(itemId: String = REMINDER_ITEM_ID) = "$REMINDER_DEEP_LINK{$itemId}"
    }

}