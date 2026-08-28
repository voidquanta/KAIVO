package com.kaivo.app.ui.navigation

import androidx.compose.runtime.Composable
import com.kaivo.app.KaivoApplication
import com.kaivo.app.ui.screens.SimpleHomeScreen

@Composable
fun KaivoNavHost(app: KaivoApplication) {
    SimpleHomeScreen()
}
