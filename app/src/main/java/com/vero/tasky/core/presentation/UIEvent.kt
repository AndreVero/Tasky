package com.vero.tasky.core.presentation

sealed interface UIEvent {
    class NavigateTo(val route: String) : UIEvent
    class ShowErrorMessage(val error: ErrorType) : UIEvent
}