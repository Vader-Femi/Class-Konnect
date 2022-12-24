package com.femi.e_class.ui

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.compose.rememberNavController
import com.femi.e_class.R
import com.femi.e_class.compose.E_ClassTheme
import com.femi.e_class.data.BottomNavItem
import com.femi.e_class.data.UserPreferences
import com.femi.e_class.databinding.ActivityHomeBinding
import com.femi.e_class.repositories.HomeActivityRepository
import com.femi.e_class.viewmodels.HomeActivityViewModel
import com.femi.e_class.viewmodels.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import org.jitsi.meet.sdk.*
import timber.log.Timber
import java.net.MalformedURLException
import java.net.URL

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeActivityViewModel

    //    private lateinit var appBarConfiguration: AppBarConfiguration
    private var hasNotificationPermission = false

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            onBroadcastReceived(intent)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setupViewModel()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setSupportActionBar(binding.toolbar)

        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                E_ClassTheme(dynamicColor = viewModel.useDynamicTheme) {
                    Surface {
                        val navController = rememberNavController()
                        var appBarTitle by remember{ mutableStateOf(getString(R.string.app_name)) }
                        Scaffold(
                            topBar = {
                                AppBar(title = appBarTitle)
                            },
                            bottomBar = {
                                BottomNavigationBar(
                                    items = listOf(
                                        BottomNavItem(
                                            name = "Profile",
                                            route = "profile",
                                            icon = Icons.Default.Person
                                        ),
                                        BottomNavItem(
                                            name = "Home",
                                            route = "home",
                                            icon = Icons.Default.Home
                                        ),
                                        BottomNavItem(
                                            name = "Settings",
                                            route = "settings",
                                            icon = Icons.Default.Settings
                                        ),
                                    ),
                                    navController = navController,
                                    onItemClick = {
                                        navController.navigate(it.route)
                                        appBarTitle = it.name
                                    }
                                )
                            },
                            content = { paddingValue ->
                                Navigation(
                                    navController = navController,
                                    viewModel = viewModel,
                                    paddingValue = paddingValue
                                )
                            }
                        )
                    }
                }
            }
        }
        checkNotificationPermission()

        val uri = intent.data
        if (uri?.pathSegments?.get(0) != null) {
            lifecycleScope.launch {
                startMeeting(uri.pathSegments[0])
            }
        }

//        val navController = findNavController(R.id.nav_host_fragment_content_home)
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)


    }

    fun checkNotificationPermission(): Boolean {
        hasNotificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }

        if (!hasNotificationPermission) {
            showNotificationDialog()
        }

        return hasNotificationPermission
    }

    private fun showNotificationDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission = isGranted
        if (!isGranted) {
            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)

        }
    }


    private fun setupViewModel() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val fireStoreReference = FirebaseFirestore.getInstance().collection("Users")
        val dataStore = UserPreferences(this)
        val repository = HomeActivityRepository(firebaseAuth, fireStoreReference, dataStore)
        val viewModelFactory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeActivityViewModel::class.java]
    }

    suspend fun startMeeting(
        courseCode: String,
//        password: String,
    ) {

        val email = viewModel.userEmail()
        val fName = viewModel.userFName()
        val matric = viewModel.userMatric()
        val videoResolution = viewModel.videoResolution()
        val displayName = "$matric-$fName"


        val serverURL: URL = try {
            // When using JaaS, replace "https://meet.jit.si" with the proper serverURL
            URL("https://meet.jit.si")
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            throw RuntimeException("Invalid server URL!")
        }

        val defaultOptions: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder()
            .setServerURL(serverURL)
            .setAudioMuted(true)
            .setVideoMuted(true)
            .setFeatureFlag("meeting-password.enabled", false)
            .setFeatureFlag("call-integration.enabled", false)
            .setFeatureFlag("calendar.enabled", false)
            .setFeatureFlag("recording.enabled", true)
            .setFeatureFlag("add-people.enabled", true)
            .setFeatureFlag("close-captions.enabled", true)
            .setFeatureFlag("chat.enabled", true)
            .setFeatureFlag("invite.enabled", true)
            .setFeatureFlag("resolution", videoResolution)
            .setFeatureFlag("live-streaming.enabled", false)
            .setFeatureFlag("meeting-name.enabled", true)
            .setFeatureFlag("pip.enabled", true)
            .setFeatureFlag("video-share.enabled", false)
            .setFeatureFlag("security-options.enabled", true)
            .setFeatureFlag("android.screensharing.enabled", true)
            .build()


        JitsiMeet.setDefaultConferenceOptions(defaultOptions)
        registerForBroadcastMessages()

        val options: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder()
            .setRoom(courseCode)
            .setSubject(courseCode)
            .setUserInfo(JitsiMeetUserInfo(
                Bundle().apply {
                    this.putString("displayName", displayName)
                    this.putString("email", email)
                }
            ))
            .build()
        JitsiMeetActivity.launch(this, options)
    }

    private fun registerForBroadcastMessages() {
        val intentFilter = IntentFilter()
        for (type in BroadcastEvent.Type.values()) {
            intentFilter.addAction(type.action)
        }

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadcastReceiver, intentFilter)
    }

    // Example for handling different JitsiMeetSDK events
    private fun onBroadcastReceived(intent: Intent?) {
        if (intent != null) {
            val event = BroadcastEvent(intent)
            when (event.type) {
                BroadcastEvent.Type.CONFERENCE_JOINED -> {
                    viewModel.classStarted()
                }
                BroadcastEvent.Type.PARTICIPANT_JOINED -> {
                    Toast.makeText(this, "${event.data["name"]} Joined", Toast.LENGTH_SHORT)
                        .show()
                }
                BroadcastEvent.Type.ENDPOINT_TEXT_MESSAGE_RECEIVED -> {

                }
                BroadcastEvent.Type.CONFERENCE_TERMINATED -> {
                    viewModel.classEnded()
                }
                else -> Timber.i("Received event: %s", event.type)
            }
        }
    }

    // Example for sending actions to JitsiMeetSDK
    private fun hangUp() {
        val hangupBroadcastIntent: Intent = BroadcastIntentHelper.buildHangUpIntent()
        LocalBroadcastManager.getInstance(this).sendBroadcast(hangupBroadcastIntent)
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_home)
//        return navController.navigateUp(appBarConfiguration)
//                || super.onSupportNavigateUp()
//    }

}