package com.vero.tasky.splash_feature.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.vero.tasky.R
import com.vero.tasky.core.presentation.navigation.Screens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navigateTo: (route: String) -> Unit,
    isLoggedIn: Boolean
) {

    LaunchedEffect(key1 = true) {
        delay(1000)
        if (isLoggedIn) {
            navigateTo(Screens.Agenda.route)
        } else {
            navigateTo(Screens.Login.route)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.secondary)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_logo_without_background),
            contentDescription = "Logo icon",
            tint = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}