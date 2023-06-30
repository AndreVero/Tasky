package com.vero.tasky.core.domain.matcher

interface EmailMatcher {
    fun matches(email: String): Boolean
}