package com.vero.tasky

import androidx.annotation.StringRes

data class MainState(
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = true,
    @StringRes val errorMessage: Int? = null,
)
