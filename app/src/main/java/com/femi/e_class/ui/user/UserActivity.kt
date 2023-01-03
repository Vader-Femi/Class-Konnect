package com.femi.e_class.ui.user

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.femi.e_class.R
import com.femi.e_class.data.BottomNavBarData
import com.femi.e_class.navigation.Screen
import com.femi.e_class.navigation.UserActivityNavigation
import com.femi.e_class.theme.E_ClassTheme
import com.femi.e_class.ui.authentiction.AuthenticationActivity
import com.femi.e_class.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.jitsi.meet.sdk.*
import java.net.MalformedURLException
import java.net.URL

@AndroidEntryPoint
class UserActivity : ComponentActivity() {

    private var hasNotificationPermission = false

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel = hiltViewModel<UserViewModel>()
            LaunchedEffect(key1 = true) {
                checkNotificationPermission()
                verifyAppLink(intent.data, viewModel)
            }
            E_ClassTheme(dynamicColor = viewModel.useDynamicTheme) {
                Surface {
                    val navController = rememberNavController()
                    var appBarTitle by remember { mutableStateOf(getString(R.string.app_name)) }
                    Scaffold(
                        topBar = {
                            AppBar(title = appBarTitle)
                        },
                        bottomBar = {
                            BottomNavigationBar(
                                items = BottomNavBarData.getItems(),
                                navController = navController,
                                onItemClick = {
                                    navController.navigate(it.route) {
                                        popUpTo(Screen.HomeScreen.route)
                                        launchSingleTop = true
                                    }
                                    appBarTitle = it.name
                                }
                            )
                        },
                        content = { paddingValue ->
                            Box(modifier = Modifier.padding(paddingValue)) {
                                UserActivityNavigation(
                                    navController = navController,
                                    viewModel = viewModel
                                )
                            }
                        }
                    )
                }
            }
        }

    }

    private fun checkNotificationPermission(): Boolean {
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

    private suspend fun verifyAppLink(uri: Uri?, viewModel: UserViewModel) {
        if (uri?.pathSegments?.get(0) != null) {
            if (viewModel.isUserNew()) {
                Toast.makeText(this, "Please Log in before joining a class", Toast.LENGTH_SHORT)
                    .show()
                Intent(this@UserActivity, AuthenticationActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                    finish()
                }
            } else {
                startMeeting(viewModel, uri.pathSegments[0])
            }
        }
    }

    suspend fun startMeeting(
        viewModel: UserViewModel,
        courseCode: String,
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
}