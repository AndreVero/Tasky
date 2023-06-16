package com.vero.tasky.ui.theme

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color

val Green = Color(0xFF279F70)
val Black = Color(0xFF16161C)
val White = Color(0xFFFFFFFF)
val Light = Color(0xFFF6F1FB)
val Light2 = Color(0xFFF2F3F7)
val Light3 = Color(0xFFEEF6FF)
val DarkGray = Color(0xFF5C5D5A)
val Gray = Color(0xFFA9B4BE)
val Purple = Color(0xFF8E97FD)
val LightGray = Color(0xFFA1A4B2)

val Colors.headerText : Color
    get() = White

val Colors.textFieldBackground : Color
    get() = Light2

val Colors.onTextFieldHint : Color
    get() = LightGray

val Colors.text : Color
    get() = DarkGray

val Colors.onTextFieldIcon : Color
    get() = Gray

val Colors.buttonBackground : Color
    get() = Black

val Colors.buttonText : Color
    get() = Light

val Colors.bottomText : Color
    get() = LightGray

val Colors.bottomTextAccent : Color
    get() = Purple

val Colors.profileIcon : Color
    get() = Light3