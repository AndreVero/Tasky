package com.vero.tasky.core.data.local

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import com.vero.tasky.core.domain.local.Preferences
import com.vero.tasky.core.domain.model.User
import javax.inject.Inject

class PreferencesImpl @Inject constructor(
    application: Application
) : Preferences {

    private val pref = application.getSharedPreferences(
        "com.vero.tasky", Context.MODE_PRIVATE)

    override fun isLoggedIn() = getUser() != null
    override fun getUser(): User? {
        val token = pref.getString(Preferences.TOKEN, null)
        val fullName = pref.getString(Preferences.FULL_NAME, null)
        if (token != null && fullName != null)
            return User(token = token, fullName = fullName)
        return null
    }

    override fun saveUser(user: User) {
        pref.edit {
            putString(Preferences.TOKEN, user.token)
            putString(Preferences.FULL_NAME, user.fullName)
            commit()
        }
    }
}