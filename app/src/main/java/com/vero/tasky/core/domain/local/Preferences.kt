package com.vero.tasky.core.domain.local

interface Preferences {

    fun isLoggedIn() : Boolean

    fun getJwtToken() : String?

    fun setJwtToken(token: String)

    companion object {
        const val JWT_TOKEN = "JWT_TOKEN"
    }
}