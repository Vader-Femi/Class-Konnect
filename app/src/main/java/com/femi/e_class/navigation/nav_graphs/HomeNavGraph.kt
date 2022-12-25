package com.femi.e_class.navigation.nav_graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.femi.e_class.ui.HomeScreen
import com.femi.e_class.viewmodels.HomeActivityViewModel

fun NavGraphBuilder.homeNavGraph(
    viewModel: HomeActivityViewModel
) {
    navigation(
        startDestination = "home",
        route = "home_route"){
        composable("home"){
            HomeScreen(viewModel = viewModel)
        }
    }
}