package com.vero.tasky.core.domain.local

import com.vero.tasky.core.domain.model.User

interface UserPreferences {

    fun isLoggedIn() : Boolean

    fun getUser(): User?

    fun saveUser(user: User)

    fun clear()

    companion object {
        const val TOKEN = "TOKEN"
        const val FULL_NAME = "FULL_NAME"
        const val USER_ID = "USER_ID"
    }
}