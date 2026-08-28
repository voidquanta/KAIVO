package com.kaivo.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kaivo.app.KaivoApplication
import com.kaivo.app.ui.screens.HomeScreen
import com.kaivo.app.ui.screens.SettingsScreen
import com.kaivo.app.viewmodel.HomeViewModel
import com.kaivo.app.viewmodel.HomeViewModelFactory
import com.kaivo.app.viewmodel.SettingsViewModel
import com.kaivo.app.viewmodel.SettingsViewModelFactory

private object Routes {
    const val HOME = "home"
    const val SETTINGS = "settings"
}

@Composable
fun KaivoNavHost(app: KaivoApplication) {
    val navController: NavHostController = rememberNavController()

    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(app.repository)
    )
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(app.settingsDataStore)
    )

    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                viewModel = homeViewModel,
                onOpenSettings = { navController.navigate(Routes.SETTINGS) }
            )
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(
                viewModel = settingsViewModel,
                onBack = { navController.popBackStack() },
                getAllItemsForExport = { homeViewModel.getAllForExport() },
                onDeleteAllConfirmed = { homeViewModel.deleteAllData() }
            )
        }
    }
}
