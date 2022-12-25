package com.femi.e_class.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.femi.e_class.navigation.nav_graphs.homeNavGraph
import com.femi.e_class.navigation.nav_graphs.profileNavGraph
import com.femi.e_class.navigation.nav_graphs.settingsNavGraph
import com.femi.e_class.viewmodels.HomeActivityViewModel

@Composable
fun Navigation(
    navController: NavHostController,
    viewModel: HomeActivityViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "home_route",
        route = "root_route") {
        homeNavGraph(viewModel)
        profileNavGraph(viewModel)
        settingsNavGraph(viewModel)
    }
}