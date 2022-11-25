package com.femi.e_class.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.femi.e_class.databinding.ActivitySignUpBinding
import com.femi.e_class.repositories.SignUpRepository
import com.femi.e_class.viewmodels.SignUpViewModel
import com.femi.e_class.viewmodels.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setupViewModel()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    private fun setupViewModel() {
        val fireStoreReference = FirebaseFirestore.getInstance().collection("Users")
        val firebaseAuth = FirebaseAuth.getInstance()
        val repository = SignUpRepository(firebaseAuth, fireStoreReference)
        val viewModelFactory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[SignUpViewModel::class.java]
    }
}