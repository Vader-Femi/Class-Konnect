package com.femi.e_class.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.femi.e_class.data.UserPreferences
import com.femi.e_class.databinding.ActivityMainBinding
import com.femi.e_class.repositories.MainActivityRepository
import com.femi.e_class.viewmodels.MainActivityViewModel
import com.femi.e_class.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            this.setKeepOnScreenCondition {
                viewModel.isLoading
            }
        }
        setupViewModel()
        lifecycleScope.launch {
            returningUserCheck()
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.btnSignIn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }


    }

    private fun setupViewModel() {
        val dataStore = UserPreferences(this)
        val repository = MainActivityRepository(dataStore)
        val viewModelFactory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainActivityViewModel::class.java]
    }


    private suspend fun returningUserCheck() {
        if (viewModel.userEmail().isNotEmpty() &&
            viewModel.userFName().isNotEmpty() &&
            viewModel.userLName().isNotEmpty() &&
            viewModel.userMatric().toString().isNotEmpty()
        ){
            Intent(this@MainActivity, HomeActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
                finish()
            }
        }
    }

}