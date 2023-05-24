package com.vero.tasky.core.presentation.components

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.staticCompositionLocalOf

val LocalScaffoldState = staticCompositionLocalOf<ScaffoldState> { error("Scaffold state wasn't provide!") }