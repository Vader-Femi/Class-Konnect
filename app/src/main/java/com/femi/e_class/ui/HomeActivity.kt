package com.femi.e_class.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.femi.e_class.KEY_EMAIL
import com.femi.e_class.databinding.ActivityHomeBinding

private lateinit var binding: ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = intent.getStringExtra(KEY_EMAIL)
        Toast.makeText(this, email.toString(), Toast.LENGTH_SHORT).show()
    }
}