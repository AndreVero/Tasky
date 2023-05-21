package com.vero.tasky.auth.domain.matcher

interface EmailMatcher {
    fun matches(email: String): Boolean
}