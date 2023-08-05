package com.femi.e_class.presentation.authentication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
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
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
            E_ClassTheme(dynamicColor = viewModel.useDynamicTheme) {
                Surface {
                    Scaffold(
                        topBar = {
                            AuthenticationAppBar(navBackStackEntry, scrollBehavior)
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        content = { paddingValue ->
                            Box(
                                modifier = Modifier
                                    .padding(paddingValue)
                            ) {
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