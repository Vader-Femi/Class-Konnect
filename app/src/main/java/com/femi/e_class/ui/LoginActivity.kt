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
import com.femi.e_class.KEY_EMAIL
import com.femi.e_class.KEY_PASSWORD
import com.femi.e_class.data.UserPreferences
import com.femi.e_class.databinding.ActivityLoginBinding
import com.femi.e_class.disable
import com.femi.e_class.presentation.LogInFormEvent
import com.femi.e_class.repositories.LogInRepository
import com.femi.e_class.viewmodels.LogInViewModel
import com.femi.e_class.viewmodels.ViewModelFactory
import com.femi.e_class.visible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LogInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setupViewModel()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val email = intent.getStringExtra(KEY_EMAIL)
        val password = intent.getStringExtra(KEY_PASSWORD)
        binding.etEmail.setText(email)
        binding.etPassword.setText(password)

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
                        is LogInViewModel.ValidationEvent.Success -> {
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
                    binding.progressBar.visible(event is LogInViewModel.LogInEvent.Loading)
                    binding.btnSignIn.disable(event is LogInViewModel.LogInEvent.Loading)
                    when (event) {
                        is LogInViewModel.LogInEvent.Success -> {
                            moveToNextActivity()
                        }
                        is LogInViewModel.LogInEvent.Error -> {
                            Toast.makeText(this@LoginActivity,
                                event.exception?.message.toString(),
                                Toast.LENGTH_SHORT)
                                .show()
                        }
                        is LogInViewModel.LogInEvent.Empty -> {
                            Toast.makeText(this@LoginActivity,
                                event.message,
                                Toast.LENGTH_SHORT)
                                .show()
                        }
                        is LogInViewModel.LogInEvent.Loading -> {}
                    }
                }
            }
        }

        binding.btnSignIn.setOnClickListener {
            viewModel.onEvent(LogInFormEvent.Submit)
            binding.emailLayout.helperText = viewModel.logInFormState.emailError
            binding.passwordLayout.helperText = viewModel.logInFormState.passwordError
//            Intent(this@LoginActivity, HomeActivity::class.java).also { intent ->
//                intent.putExtra(KEY_EMAIL, "aaa@gmail.com")
//                startActivity(intent)
//            }
        }
    }

    private fun moveToNextActivity(){
        Intent(this@LoginActivity, HomeActivity::class.java).also { intent ->
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra(KEY_EMAIL, viewModel.logInFormState.email)
            startActivity(intent)
            finish()
        }
    }

    private fun setupViewModel() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val fireStoreReference = FirebaseFirestore.getInstance().collection("Users")
        val dataStore = UserPreferences(this)
        val repository = LogInRepository(firebaseAuth, fireStoreReference, dataStore)
        val viewModelFactory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[LogInViewModel::class.java]
    }
}