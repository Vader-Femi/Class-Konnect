package com.femi.e_class.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.femi.e_class.viewmodels.HomeActivityViewModel

@Composable
fun Navigation(navController: NavHostController, viewModel: HomeActivityViewModel, paddingValue: PaddingValues){
    NavHost(navController = navController, startDestination = "home"){
        composable("home"){
            Box(modifier = Modifier.padding(paddingValue)) {
                HomeScreen(viewModel = viewModel)
            }
        }
        composable("profile"){
            Box(modifier = Modifier.padding(paddingValue)) {
                ProfileScreen(viewModel = viewModel)
            }
        }
        composable("settings"){
            Box(modifier = Modifier.padding(paddingValue)) {
                SettingsScreen(viewModel = viewModel)
            }
        }
    }
}