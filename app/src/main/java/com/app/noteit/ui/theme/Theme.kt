package com.app.noteit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    onPrimary = OnPrimaryColor,
    onSecondary = OnSecondaryColor,
    background = BackgroundColor,
)

private val LightColorPalette = lightColors(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    onPrimary = OnPrimaryColor,
    onSecondary = OnSecondaryColor,
    background = BackgroundColor,
)

@Composable
fun NoteItTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}