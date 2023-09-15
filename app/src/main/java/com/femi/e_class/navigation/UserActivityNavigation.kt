package com.femi.e_class.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.femi.e_class.ui.user.home.HomeScreen
import com.femi.e_class.ui.user.profile.ProfileScreen
import com.femi.e_class.ui.user.settings.SettingsScreen
import com.femi.e_class.viewmodels.UserViewModel

@Composable
fun UserActivityNavigation(
    navController: NavHostController,
    viewModel: UserViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route,
        route = Screen.UserRoute.route) {
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(viewModel)
        }
        composable(route = Screen.ProfileScreen.route) {
            ProfileScreen(viewModel)
        }
        composable(route = Screen.SettingsScreen.route) {
            SettingsScreen(viewModel)
        }
    }
}