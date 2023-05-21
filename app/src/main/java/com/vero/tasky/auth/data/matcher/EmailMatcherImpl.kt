package com.vero.tasky.auth.data.matcher

import android.util.Patterns
import com.vero.tasky.auth.domain.matcher.EmailMatcher

class EmailMatcherImpl : EmailMatcher {
    
    override fun matches(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    
}