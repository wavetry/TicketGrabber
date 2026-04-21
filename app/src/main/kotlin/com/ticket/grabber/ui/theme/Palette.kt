package com.ticket.grabber.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * 应用色板配置
 */
val Blue700 = Color(0xFF1976D2)
val Blue400 = Color(0xFF64B5F6)
val Blue50 = Color(0xFFE3F2FD)

val DarkColorScheme = darkColorScheme(
    primary = Blue700,
    onPrimary = Color.White,
    primaryContainer = Blue400,
    onPrimaryContainer = Color.Black,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF121212),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF202020),
    onSurfaceVariant = Color(0xFFB0B0B0),
    outline = Color(0xFF606060),
    error = Color(0xFFCF6679),
    onError = Color.Black
)

val LightColorScheme = lightColorScheme(
    primary = Blue700,
    onPrimary = Color.White,
    primaryContainer = Blue400,
    onPrimaryContainer = Color.Black,
    background = Color(0xFFFFFFFF),
    onBackground = Color.Black,
    surface = Color(0xFFFFFFFF),
    onSurface = Color.Black,
    surfaceVariant = Blue50,
    onSurfaceVariant = Color(0xFF404040),
    outline = Color(0xFF808080),
    error = Color(0xFFB00020),
    onError = Color.White
)
