package com.femi.e_class.ui.authentication

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import com.femi.e_class.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticationAppBar(
    navBackStackEntry: NavBackStackEntry?,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val originalRoute = navBackStackEntry?.destination?.route
    val routeWithoutArg = if (originalRoute?.contains("?") == true) {
        originalRoute.split("?")[0]
    } else
        originalRoute
    var bigTitle by remember { mutableStateOf("") }
    var smallTitle by remember { mutableStateOf("") }

    when (routeWithoutArg) {
        Screen.LogInScreen.route -> {
            bigTitle = "Sign In"
            smallTitle = "Enter your details to get started"
        }

        Screen.SignUpScreen.route -> {
            bigTitle = "Create your account"
            smallTitle = "Enter your details to get started"
        }

        Screen.ResetPasswordScreen.route -> {
            bigTitle = "Reset Password"
            smallTitle = "Enter your password to receive reset link"
        }
    }

    when (routeWithoutArg) {
        Screen.OnBoardingScreen.route -> {

        }
        else -> {
            LargeTopAppBar(
                title = {
                    Column() {
                        Text(
                            text = bigTitle,
                            style = TextStyle.Default.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 24.sp,
                                textAlign = TextAlign.Start
                            )
                        )
                        Text(
                            text = smallTitle,
                            style = TextStyle.Default.copy(
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Start,
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(),
                scrollBehavior = scrollBehavior
            )
        }

    }
}