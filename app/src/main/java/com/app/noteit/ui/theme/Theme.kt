package com.app.noteit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColorScheme(
    primary = colorPrimary,
    secondary = colorSecondary,
    onPrimary = colorOnPrimary,
    onSecondary = colorOnSecondary,
    background = colorBackground,
    onBackground = colorOnBackground,
    surface = colorSurface,
    onSurface = colorOnSurface,
    surfaceVariant = colorSurfaceVariant,
    onSurfaceVariant = colorOnSurfaceVariant
)

private val LightColorPalette = lightColorScheme(
    primary = colorPrimary,
    secondary = colorSecondary,
    onPrimary = colorOnPrimary,
    onSecondary = colorOnSecondary,
    background = colorBackground,
    onBackground = colorOnBackground,
    surface = colorSurface,
    onSurface = colorOnSurface,
    surfaceVariant = colorSurfaceVariant,
    onSurfaceVariant = colorOnSurfaceVariant
)

@Composable
fun NoteItTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}