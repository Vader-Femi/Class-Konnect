package com.femi.e_class.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.femi.e_class.KEY_EMAIL
import com.femi.e_class.R
import com.femi.e_class.databinding.ActivityLoginBinding
import com.femi.e_class.presentation.LogInFormEvent
import com.femi.e_class.repositories.LogInRepository
import com.femi.e_class.viewmodels.LogInViewModel
import com.femi.e_class.viewmodels.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LogInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setupViewModel()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.etEmail.doOnTextChanged { text, _, _, _ ->
            viewModel.onEvent(LogInFormEvent.EmailChanged(text.toString()))
        }

        binding.etPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.onEvent(LogInFormEvent.PasswordChanged(text.toString()))
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.validationEvents.collect { event ->
                    when (event) {
                        LogInViewModel.ValidationEvent.Success -> {
                            viewModel.logInUser(
                                email = viewModel.logInFormState.email,
                                password = viewModel.logInFormState.password
                            )
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.logInEvents.collect { event ->
                    when (event) {
                        LogInViewModel.LogInEvent.Success -> {

                            Intent(this@LoginActivity, HomeActivity::class.java).also { intent ->
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra(KEY_EMAIL, viewModel.logInFormState.email)
                                startActivity(intent)
                                finish()
                            }

                        }
                        LogInViewModel.LogInEvent.Failed -> {
                            Toast.makeText(this@LoginActivity,
                                getString(R.string.register_error_message),
                                Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }

        binding.btnSignIn.setOnClickListener {
            viewModel.onEvent(LogInFormEvent.Submit)
            binding.emailLayout.helperText = viewModel.logInFormState.emailError
            binding.passwordLayout.helperText = viewModel.logInFormState.passwordError
        }

    }

    private fun setupViewModel() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val repository = LogInRepository(firebaseAuth)
        val viewModelFactory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[LogInViewModel::class.java]
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_login)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}