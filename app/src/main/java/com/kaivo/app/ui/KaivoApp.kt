package com.kaivo.app.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.kaivo.app.KaivoApplication
import com.kaivo.app.data.ThemeMode
import com.kaivo.app.ui.navigation.KaivoNavHost
import com.kaivo.app.ui.screens.OnboardingScreen
import com.kaivo.app.ui.theme.KaivoTheme
import kotlinx.coroutines.launch

/**
 * Root composable: resolves light/dark theme from the stored preference
 * (falling back to the system setting), and shows the one-time onboarding
 * screen before handing off to the main nav graph.
 */
@Composable
fun KaivoApp(app: KaivoApplication) {
    val scope = rememberCoroutineScope()

    val themeMode by app.settingsDataStore.themeMode.collectAsState(initial = ThemeMode.SYSTEM)
    val onboardingSeen by app.settingsDataStore.onboardingSeen.collectAsState(initial = false)

    val systemIsDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> systemIsDark
    }

    KaivoTheme(darkTheme = isDark) {
        if (onboardingSeen) {
            KaivoNavHost(app = app)
        } else {
            OnboardingScreen(onGetStarted = {
                scope.launch { app.settingsDataStore.setOnboardingSeen() }
            })
        }
    }
}
