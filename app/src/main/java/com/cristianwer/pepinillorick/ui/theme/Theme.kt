package com.cristianwer.pepinillorick.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = RickGreen,
    secondary = RickBlue,
    tertiary = NeonFavorite,
    background = DeepSpace,
    surface = DarkSurface,
    onPrimary = DeepSpace,
    onSecondary = OffWhite,
    onTertiary = DeepSpace,
    onBackground = OffWhite,
    onSurface = OffWhite,
    error = ErrorDark
)

private val LightColorScheme = lightColorScheme(
    primary = RickGreenDark,
    secondary = RickBlue,
    tertiary = NeonFavorite,
    background = OffWhite,
    surface = androidx.compose.ui.graphics.Color.White,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    onTertiary = androidx.compose.ui.graphics.Color.White,
    onBackground = DeepSpace,
    onSurface = DeepSpace,
    error = ErrorLight
)

/**
 * Main theme for the Pepinillo Rick application.
 *
 * @param darkTheme Whether the dark theme is enabled.
 * @param dynamicColor Whether to use Material You dynamic colors (Android 12+).
 * @param content The composable content to be themed.
 */
@Composable
fun PepinilloRickTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
