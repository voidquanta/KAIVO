package com.kaivo.app.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.kaivo.app.KaivoApplication
import com.kaivo.app.data.ThemeMode
import com.kaivo.app.ui.navigation.KaivoNavHost
import com.kaivo.app.ui.theme.KaivoTheme

/**
 * Root composable: resolves light/dark theme from the stored preference
 * (falling back to the system setting), and shows the main app navigation.
 */
@Composable
fun KaivoApp(app: KaivoApplication) {
    val themeMode by app.settingsDataStore.themeMode.collectAsState(initial = ThemeMode.SYSTEM)

    val systemIsDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> systemIsDark
    }

    KaivoTheme(darkTheme = isDark) {
        KaivoNavHost(app = app)
    }
}
