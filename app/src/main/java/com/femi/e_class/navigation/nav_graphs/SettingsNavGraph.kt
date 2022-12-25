package com.femi.e_class.navigation.nav_graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.femi.e_class.ui.SettingsScreen
import com.femi.e_class.viewmodels.HomeActivityViewModel

fun NavGraphBuilder.settingsNavGraph(
    viewModel: HomeActivityViewModel
) {
    navigation(
        startDestination = "settings",
        route = "settings_route"){
        composable("settings"){
            SettingsScreen(viewModel = viewModel)
        }
    }
}