package com.femi.e_class.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.femi.e_class.viewmodels.AuthenticationViewModel
import com.femi.e_class.ui.authentication.login.LogInScreen
import com.femi.e_class.ui.authentication.onboarding.OnBoadingScreen
import com.femi.e_class.ui.authentication.resetpassword.ResetPasswordScreen
import com.femi.e_class.ui.authentication.signup.SignUpScreen

@Composable
fun AuthenticationNavigation(
    viewModel: AuthenticationViewModel,
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = Screen.OnBoardingScreen.route,
        route = Screen.AuthenticationRoute.route
    ) {

        composable(route = Screen.OnBoardingScreen.route) {
            OnBoadingScreen(navController = navController)
        }

        composable(route = Screen.SignUpScreen.route) {
            SignUpScreen(navController = navController, viewModel = viewModel)
        }

        composable(
            route = Screen.LogInScreen.route + "?email={email}?password={password}",
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("password") {
                    type = NavType.StringType
                    nullable = true
                },
            )
        ) { entry ->
            LogInScreen(
                navController = navController,
                viewModel = viewModel,
                email = entry.arguments?.getString("email"),
                password = entry.arguments?.getString("password")
            )
        }

        composable(route = Screen.ResetPasswordScreen.route) {
            ResetPasswordScreen(navController = navController, viewModel = viewModel)
        }

    }
}