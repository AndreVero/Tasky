package com.vero.tasky.core.data.local

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.vero.tasky.core.domain.local.UserPreferences
import com.vero.tasky.core.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserPreferencesImpl @Inject constructor(
    @ApplicationContext context: Context
) : UserPreferences {

    private val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val pref = EncryptedSharedPreferences.create(
        "com.vero.tasky",
        masterKey,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override fun isLoggedIn() = getUser() != null
    override fun getUser(): User? {
        val token = pref.getString(UserPreferences.TOKEN, null)
        val fullName = pref.getString(UserPreferences.FULL_NAME, null)
        val userId = pref.getString(UserPreferences.USER_ID, null)
        if (token != null && fullName != null && userId != null)
            return User(token = token, fullName = fullName, userId = userId)
        return null
    }

    override fun saveUser(user: User) {
        pref.edit {
            putString(UserPreferences.TOKEN, user.token)
            putString(UserPreferences.FULL_NAME, user.fullName)
            putString(UserPreferences.USER_ID, user.userId)
        }
    }

    override fun clear() {
        pref.edit {
            clear()
        }
    }
}