package com.femi.e_class.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.facebook.react.modules.core.PermissionListener
import com.femi.e_class.*
import com.femi.e_class.databinding.ActivityHomeBinding
import com.femi.e_class.repositories.HomeActivityRepository
import com.femi.e_class.repositories.SignUpRepository
import com.femi.e_class.viewmodels.HomeActivityViewModel
import com.femi.e_class.viewmodels.SignUpViewModel
import com.femi.e_class.viewmodels.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate
import org.jitsi.meet.sdk.JitsiMeetActivityInterface

class HomeActivity : AppCompatActivity(), JitsiMeetActivityInterface {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeActivityViewModel
    private var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setupViewModel()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        email = intent.getStringExtra(KEY_EMAIL)
        if (email != null)
            getUserDetails()
        else
            finish()

        val loadingAlertDialog: AlertDialog = MaterialAlertDialogBuilder(this)
            .setTitle("Welcome Back")
            .setView(layoutInflater.inflate(R.layout.loading, null))
            .setMessage("Logging In...Please Wait")
            .setCancelable(false)
            .create()


        lifecycleScope.launch {
            viewModel.getUserEvent.collect { event ->
                loadingAlertDialog.showLoadingDialog(event is HomeActivityViewModel.GetUserEvent.Loading)
                when (event) {
                    is HomeActivityViewModel.GetUserEvent.Success -> {

                    }
                    is HomeActivityViewModel.GetUserEvent.Error -> {
                        MaterialAlertDialogBuilder(this@HomeActivity)
                            .setTitle("Interesting")
                            .setMessage(event.exception?.message.toString())
                            .setPositiveButton("Retry"){ _,_ ->
                                getUserDetails()
                            }
                            .setNegativeButton("Cancel"){ _,_ ->
                                finish()
                            }
                            .setCancelable(false)
                            .show()
                        getUserDetails()
                    }
                    is HomeActivityViewModel.GetUserEvent.Loading -> {
                    }
                }
            }
        }

    }

    private fun getUserDetails() {
        viewModel.getUser(email!!)
    }

    private fun setupViewModel() {
        val fireStoreReference = FirebaseFirestore.getInstance().collection("Users")
        val repository = HomeActivityRepository(fireStoreReference)
        val viewModelFactory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeActivityViewModel::class.java]
    }

    override fun requestPermissions(p0: Array<out String>?, p1: Int, p2: PermissionListener?) {
        JitsiMeetActivityDelegate.requestPermissions(this, p0, p1, p2)
    }

}