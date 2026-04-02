package com.technet.olttv.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TvDarkScheme = darkColorScheme(
    primary = Color(0xFF2563EB),
    secondary = Color(0xFF06B6D4),
    background = Color(0xFF07111F),
    surface = Color(0xFF0D1B2E),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun OLTTVMonitorTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = TvDarkScheme,
        content = content
    )
}
