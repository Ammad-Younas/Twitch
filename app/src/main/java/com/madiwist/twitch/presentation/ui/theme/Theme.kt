package com.madiwist.twitch.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    background = black,
    onBackground = lightGray,
    primary = green,
    onPrimary = white,
    surface = darkGray,
    onSurface = mediumGray
)
@Composable
fun TwitchTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}