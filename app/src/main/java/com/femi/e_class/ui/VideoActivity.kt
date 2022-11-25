package com.femi.e_class.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.facebook.react.modules.core.PermissionListener
import com.femi.e_class.KEY_COURSE_CODE
import com.femi.e_class.KEY_PASSWORD
import com.femi.e_class.R
import com.femi.e_class.data.UserPreferences
import com.femi.e_class.databinding.ActivityVideoBinding
import com.femi.e_class.loadingDialog
import com.femi.e_class.repositories.VideoActivityRepository
import com.femi.e_class.viewmodels.VideoActivityViewModel
import com.femi.e_class.viewmodels.ViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import org.jitsi.meet.sdk.*
import java.net.URL

class VideoActivity : AppCompatActivity(), JitsiMeetActivityInterface {

    private lateinit var binding: ActivityVideoBinding
    private lateinit var viewModel: VideoActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val courseCode = intent.getStringExtra(KEY_COURSE_CODE) ?: ""
        val password = intent.getStringExtra(KEY_PASSWORD) ?: ""

        lifecycleScope.launch {
            val email = viewModel.userEmail()
            val fName = viewModel.userFName()
            val matric = viewModel.userMatric()
            val displayName = "$matric-$fName"
            startMeeting(courseCode, password, displayName, email)
        }

    }

    private fun startMeeting(
        courseCode: String,
        password: String,
        displayName: String,
        email: String,
    ) {

        val roomName = "LASU${courseCode.uppercase()}"
        val jitsiView = JitsiMeetView(this)
        val options: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder()
            .setServerURL(URL("https://meet.jit.si"))
            .setRoom(roomName)
            .setAudioMuted(true)
            .setVideoMuted(true)
            .setSubject(roomName)
//                .setToken(password)
//                .setFeatureFlag("recording.enabled", true)
            .setUserInfo(JitsiMeetUserInfo(
                Bundle().apply {
                    this.putString("displayName", displayName)
                    this.putString("email", email)
                }
            ))

            .setAudioMuted(true)
            .setVideoMuted(true)
            .setAudioOnly(false)
            .build()

        jitsiView.join(options)
        setContentView(jitsiView)
    }

    private fun setupViewModel() {
        val fireStoreReference = FirebaseFirestore.getInstance().collection("Users")
        val dataStore = UserPreferences(this)
        val repository = VideoActivityRepository(fireStoreReference, dataStore)
        val viewModelFactory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[VideoActivityViewModel::class.java]
    }

    override fun requestPermissions(p0: Array<out String>?, p1: Int, p2: PermissionListener?) {
        JitsiMeetActivityDelegate.requestPermissions(this, p0, p1, p2)
    }

    override fun onPause() {
        super.onPause()
        JitsiMeetActivityDelegate.onHostPause(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        JitsiMeetActivityDelegate.onHostDestroy(this)
    }

}