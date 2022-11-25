package com.femi.e_class.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.facebook.react.modules.core.PermissionListener
import com.femi.e_class.*
import com.femi.e_class.data.UserPreferences
import com.femi.e_class.databinding.ActivityHomeBinding
import com.femi.e_class.repositories.HomeActivityRepository
import com.femi.e_class.viewmodels.HomeActivityViewModel
import com.femi.e_class.viewmodels.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate
import org.jitsi.meet.sdk.JitsiMeetActivityInterface

class HomeActivity : AppCompatActivity(), JitsiMeetActivityInterface {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeActivityViewModel
    private var email = ""
    private var matric = 0L
    private var fName = ""
    private var lName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setupViewModel()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        lifecycleScope.launch {
//            val view = layoutInflater.inflate(R.layout.loading, null)
//            val loadingAlertDialog = loadingDialog(
//                view = view,
//                title = "Welcome Back",
//                message = "Logging In...Please Wait")
//            loadingAlertDialog.show()
//            email = viewModel.userEmail()
//            matric = viewModel.userMatric()
//            fName = viewModel.userFName()
//            lName = viewModel.userLName()
//            loadingAlertDialog.hide()
//
//        }

    }

    private fun setupViewModel() {
        val fireStoreReference = FirebaseFirestore.getInstance().collection("Users")
        val dataStore = UserPreferences(this)
        val repository = HomeActivityRepository(fireStoreReference, dataStore)
        val viewModelFactory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeActivityViewModel::class.java]
    }

    override fun requestPermissions(p0: Array<out String>?, p1: Int, p2: PermissionListener?) {
        JitsiMeetActivityDelegate.requestPermissions(this, p0, p1, p2)
    }

}