package com.vero.tasky.agenda.presentation.ext

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

fun Context.hasNotificationPermission() : Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
       PackageManager.PERMISSION_GRANTED ==
               ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
    } else {
        true
    }
}