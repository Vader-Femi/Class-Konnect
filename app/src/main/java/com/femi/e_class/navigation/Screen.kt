package com.femi.e_class.navigation

sealed class Screen(val route: String) {
    object AuthenticationRoute : Screen("authentication_route")
    object SignUpScreen : Screen("sign_up_screen")
    object LogInScreen : Screen("log_in_screen")
    object ResetPasswordScreen : Screen("reset_password_screen")
    object OnBoardingScreen : Screen("on_boarding_screen")
    object UserRoute : Screen("user_route")
    object HomeScreen : Screen("home_screen")
    object ProfileScreen : Screen("profile_screen")
    object SettingsScreen : Screen("settings_screen")

    fun withArgs(vararg args: Pair<String, String>): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("?${arg.first}=${arg.second}")
            }
        }
    }
}
