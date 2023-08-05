package com.femi.e_class.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.femi.e_class.presentation.authentication.AuthenticationActivity
import com.femi.e_class.presentation.User.UserActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {

            installSplashScreen().setKeepOnScreenCondition { true }

            if (viewModel.isUserNew()) {
                goToAuthenticationActivity()
            } else {
                goToUserActivity()
            }

        }

    }

    private fun goToAuthenticationActivity() {
        Intent(this@MainActivity, AuthenticationActivity::class.java).also {
            it.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
            finish()
        }
    }

    private fun goToUserActivity() {
        Intent(this@MainActivity, UserActivity::class.java).also {
            it.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
            finish()
        }
    }


}