package com.femi.e_class.navigation.nav_graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.femi.e_class.ui.ProfileScreen
import com.femi.e_class.viewmodels.HomeActivityViewModel

fun NavGraphBuilder.profileNavGraph(
    viewModel: HomeActivityViewModel
) {

    navigation(
        startDestination = "profile",
        route = "profile_route"){
        composable("profile"){
            ProfileScreen(viewModel = viewModel)
        }
    }

}