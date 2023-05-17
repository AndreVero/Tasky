package com.vero.tasky.core.data.local

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import com.vero.tasky.core.domain.local.Preferences
import javax.inject.Inject

class PreferencesImpl @Inject constructor(
    application: Application
) : Preferences {

    private val pref = application.getSharedPreferences(
        "com.vero.tasky", Context.MODE_PRIVATE)

    override fun isLoggedIn() = getJwtToken() != null

    override fun getJwtToken(): String? {
        return pref.getString(Preferences.JWT_TOKEN, null)
    }

    override fun setJwtToken(token: String) {
        pref.edit {
            putString(Preferences.JWT_TOKEN, token)
            commit()
        }
    }
}