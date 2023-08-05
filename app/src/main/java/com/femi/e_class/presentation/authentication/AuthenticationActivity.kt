package com.femi.e_class.presentation.authentication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.femi.e_class.navigation.AuthenticationNavigation
import com.femi.e_class.theme.E_ClassTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = hiltViewModel<AuthenticationViewModel>()
            val navController = rememberNavController()
            E_ClassTheme(dynamicColor = viewModel.useDynamicTheme) {
                Surface {
                    Scaffold(
                        content = { paddingValue ->
                            Box(modifier = Modifier.padding(paddingValue)) {
                                AuthenticationNavigation(
                                    viewModel = viewModel,
                                    navController = navController
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}