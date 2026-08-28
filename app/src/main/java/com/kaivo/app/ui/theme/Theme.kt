package com.kaivo.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val KaivoLightColors = lightColorScheme(
    primary = PureBlack,
    onPrimary = PureWhite,
    secondary = PureBlack,
    onSecondary = PureWhite,
    background = PureWhite,
    onBackground = PureBlack,
    surface = PureWhite,
    onSurface = PureBlack,
    surfaceVariant = LightSurface,
    onSurfaceVariant = LightSecondaryText,
    outline = LightBorder,
    error = PureBlack,
    onError = PureWhite
)

private val KaivoDarkColors = darkColorScheme(
    primary = PureWhite,
    onPrimary = PureBlack,
    secondary = PureWhite,
    onSecondary = PureBlack,
    background = PureBlack,
    onBackground = PureWhite,
    surface = PureBlack,
    onSurface = PureWhite,
    surfaceVariant = DarkSurface,
    onSurfaceVariant = DarkSecondaryText,
    outline = DarkBorder,
    error = PureWhite,
    onError = PureBlack
)

@Composable
fun KaivoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) KaivoDarkColors else KaivoLightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = KaivoTypography,
        content = content
    )
}
