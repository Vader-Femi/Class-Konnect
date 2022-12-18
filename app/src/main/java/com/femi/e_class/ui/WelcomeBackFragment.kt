package com.femi.e_class.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.femi.e_class.R
import com.femi.e_class.compose.E_ClassTheme
import com.femi.e_class.databinding.FragmentWelcomeBackBinding
import com.femi.e_class.viewmodels.BaseViewModel
import com.femi.e_class.viewmodels.HomeActivityViewModel
import kotlinx.coroutines.launch
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetActivity
import java.time.LocalDateTime
import java.util.*

class WelcomeBackFragment : Fragment() {

    private var _binding: FragmentWelcomeBackBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<HomeActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWelcomeBackBinding.inflate(inflater, container, false)
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                E_ClassTheme(dynamicColor = viewModel.useDynamicTheme) {
                    Surface {
                        val scrollState = rememberScrollState()
                        var greetingText by remember { mutableStateOf("") }
                        var isInClass by remember { mutableStateOf(false) }
                        val context = LocalContext.current
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .verticalScroll(scrollState)
                                .fillMaxSize()
                                .padding(0.dp, 0.dp, 0.dp, 30.dp)
                        ) {
                            LaunchedEffect(key1 = Unit) {
                                    var greeting = "Good Day"
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        val calendar = Calendar.getInstance()
                                        val current = LocalDateTime.of(
                                            calendar.get(Calendar.YEAR),
                                            calendar.get(Calendar.MONTH),
                                            calendar.get(Calendar.DAY_OF_MONTH),
                                            calendar.get(Calendar.HOUR_OF_DAY),
                                            calendar.get(Calendar.MINUTE),
                                            calendar.get(Calendar.SECOND))
                                        greeting = when (current.hour) {
                                            in 6..11 -> "Good Morning"
                                            in 12..16 -> "Good Afternoon"
                                            in 17..22 -> "Good Evening"
                                            else -> "You should be sleeping"
                                        }
                                    }
                                    greetingText = "$greeting ${viewModel.userFName()}"
                            }
                            LaunchedEffect(key1 = viewModel.classStatus) {
                                viewModel.classStatus.collect { event ->
                                    isInClass = when (event) {
                                        is BaseViewModel.ClassStatus.Started -> {
                                            true
                                        }
                                        is BaseViewModel.ClassStatus.Ended -> {
                                            false
                                        }
                                    }
                                }
                            }
                            Text(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(20.dp, 0.dp, 20.dp, 0.dp),
                                text = greetingText,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 24.sp,
                                textAlign = TextAlign.Start
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            if (isInClass) {
                                OnGoingClassCard(
                                    painter = painterResource(id = R.drawable.status_update_amico),
                                    contentDescription = "Return To Class card",
                                    title = "Click To Return To OnGoing Class Screen",
                                    onClick = {
                                        JitsiMeetActivity.launch(context,
                                            JitsiMeet.getCurrentConference())
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            ImageCard(
                                painter = painterResource(id = R.drawable.status_update_amico),
                                contentDescription = "Update profile card",
                                title = "Update Profile",
                                onClick = {
                                    findNavController().navigate(R.id.action_welcomeBack_to_updateProfile)
                                }
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            ImageCard(
                                painter = painterResource(id = R.drawable.online_learning_rafiki),
                                contentDescription = "Start or join a class card",
                                title = "Start or join a class",
                                onClick = {
                                    findNavController().navigate(R.id.action_welcomeBack_to_classDetails)
                                }
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            ImageCard(
                                painter = painterResource(id = R.drawable.personal_data_amico),
                                contentDescription = "Account settings card",
                                title = "Account settings",
                                onClick = {
                                    findNavController().navigate(R.id.action_welcomeBack_to_my_account)
                                }
                            )
                        }
                    }
                }
            }
        }
        return binding.root
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ImageCard(
        painter: Painter,
        contentDescription: String,
        title: String,
        modifier: Modifier = Modifier,
        onClick: () -> Unit,
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(20.dp, 0.dp, 20.dp, 0.dp),
            shape = AbsoluteCutCornerShape(15.dp),
            onClick = onClick
        ) {
            Box(modifier = Modifier
                .height(150.dp)
                .fillMaxWidth()
            ) {
                Image(
                    painter = painter,
                    contentDescription = contentDescription,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 20.dp, bottom = 60.dp),
                    contentScale = ContentScale.FillHeight
                )
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.primaryContainer
                        ),
                        startY = 0f
                    ))) {
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Text(
                        text = title,
                        style = TextStyle(color = MaterialTheme.colorScheme.onPrimaryContainer),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun OnGoingClassCard(
        painter: Painter,
        contentDescription: String,
        title: String,
        modifier: Modifier = Modifier,
        onClick: () -> Unit,
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth(),
            shape = AbsoluteCutCornerShape(0.dp),
            onClick = onClick
        ) {
            Box(modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
            ) {
                Image(
                    painter = painter,
                    contentDescription = contentDescription,
                    modifier = Modifier.align(Alignment.CenterEnd),
                    contentScale = ContentScale.FillHeight
                )
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.primaryContainer
                        ),
                        startX = 300f
                    ))) {
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = 20.dp,
                            end = 150.dp
                        ),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = title,
                        style = TextStyle(color = MaterialTheme.colorScheme.onPrimaryContainer),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}