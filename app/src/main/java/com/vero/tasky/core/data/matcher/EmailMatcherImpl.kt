package com.vero.tasky.core.data.matcher

import android.util.Patterns
import com.vero.tasky.core.domain.matcher.EmailMatcher

class EmailMatcherImpl : EmailMatcher {
    
    override fun matches(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    
}