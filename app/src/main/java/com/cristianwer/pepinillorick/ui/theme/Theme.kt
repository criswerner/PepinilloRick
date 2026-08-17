package com.cristianwer.pepinillorick.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = RickGreen,
    secondary = RickBlue,
    tertiary = MortyYellow,
    background = DeepSpace,
    surface = DarkSurface,
    onPrimary = DeepSpace,
    onSecondary = OffWhite,
    onTertiary = DeepSpace,
    onBackground = OffWhite,
    onSurface = OffWhite
)

private val LightColorScheme = lightColorScheme(
    primary = RickGreen,
    secondary = RickBlue,
    tertiary = MortyYellow,
    background = OffWhite,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = DeepSpace,
    onSurface = DeepSpace
)

@Composable
fun PepinilloRickTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}