package com.femi.e_class.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.femi.e_class.ui.authentiction.AuthenticationActivity
import com.femi.e_class.ui.user.UserActivity
import com.femi.e_class.viewmodels.MainActivityViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = hiltViewModel<MainActivityViewModel>()
            LaunchedEffect(key1 = true) {
                installSplashScreen().apply {
                    this.setKeepOnScreenCondition {
                        viewModel.isLoading
                    }
                }

                if (isUserNew(viewModel)) {
                    Intent(this@MainActivity, AuthenticationActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                        finish()
                    }
                } else {
                    Intent(this@MainActivity, UserActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                        finish()
                    }
                }
            }
        }
    }


    private suspend fun isUserNew(viewModel: MainActivityViewModel): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val email = viewModel.userEmail()

        val isReturningUser = currentUser != null &&
                email.isNotEmpty() &&
                currentUser.email == email &&
                viewModel.userFName().isNotEmpty() &&
                viewModel.userLName().isNotEmpty() &&
                viewModel.userMatric() != 0L


        return !isReturningUser
    }

}