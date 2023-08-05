package com.femi.e_class.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.femi.e_class.presentation.User.Home.HomeScreen
import com.femi.e_class.presentation.User.Profile.ProfileScreen
import com.femi.e_class.presentation.User.Settings.SettingsScreen
import com.femi.e_class.presentation.User.UserViewModel

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