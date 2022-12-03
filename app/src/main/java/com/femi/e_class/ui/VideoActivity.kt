package com.femi.e_class.ui

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telecom.TelecomManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.femi.e_class.KEY_COURSE_CODE
import com.femi.e_class.KEY_PASSWORD
import com.femi.e_class.data.UserPreferences
import com.femi.e_class.databinding.ActivityVideoBinding
import com.femi.e_class.repositories.VideoActivityRepository
import com.femi.e_class.viewmodels.VideoActivityViewModel
import com.femi.e_class.viewmodels.ViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import org.jitsi.meet.sdk.*
import timber.log.Timber
import java.net.MalformedURLException
import java.net.URL


class VideoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoBinding
    private lateinit var viewModel: VideoActivityViewModel

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            onBroadcastReceived(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
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

        binding.btnHangUp.setOnClickListener {
            hangUp()
            finish()
        }

    }

    private fun startMeeting(
        courseCode: String,
        password: String,
        displayName: String,
        email: String,
    ) {

        val roomName = "LASU${courseCode.uppercase()}"
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
            .setSubject(roomName)
//                .setToken(password)
//                .setFeatureFlag("recording.enabled", true)
            .build()

        JitsiMeet.setDefaultConferenceOptions(defaultOptions)
        registerForBroadcastMessages()

        val options: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder()
            .setRoom(roomName)
            .setSubject(roomName)
            .setUserInfo(JitsiMeetUserInfo(
                Bundle().apply {
                    this.putString("displayName", displayName)
                    this.putString("email", email)
                }
            ))
            .build()
        JitsiMeetActivity.launch(this, options)

    }

    private fun setupViewModel() {
        val fireStoreReference = FirebaseFirestore.getInstance().collection("Users")
        val dataStore = UserPreferences(this)
        val repository = VideoActivityRepository(fireStoreReference, dataStore)
        val viewModelFactory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[VideoActivityViewModel::class.java]
    }

    private fun registerForBroadcastMessages() {
        val intentFilter = IntentFilter()

        /* This registers for every possible event sent from JitsiMeetSDK
           If only some of the events are needed, the for loop can be replaced
           with individual statements:
           ex:  intentFilter.addAction(BroadcastEvent.Type.AUDIO_MUTED_CHANGED.action);
                intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_TERMINATED.action);
                ... other events
         */
        for (type in BroadcastEvent.Type.values()) {
            intentFilter.addAction(type.action)
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter)
    }

    // Example for handling different JitsiMeetSDK events
    private fun onBroadcastReceived(intent: Intent?) {
        if (intent != null) {
            val event = BroadcastEvent(intent)
            when (event.type) {
                BroadcastEvent.Type.CONFERENCE_JOINED -> Timber.i("Conference Joined with url%s",
                    event.data["url"])
                BroadcastEvent.Type.PARTICIPANT_JOINED -> Timber.i("Participant joined%s",
                    event.data["name"])
                else -> Timber.i("Received event: %s", event.type)
            }
        }
    }

    // Example for sending actions to JitsiMeetSDK
    private fun hangUp() {
        val hangupBroadcastIntent: Intent = BroadcastIntentHelper.buildHangUpIntent()
        LocalBroadcastManager.getInstance(this.applicationContext).sendBroadcast(hangupBroadcastIntent)
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }

}