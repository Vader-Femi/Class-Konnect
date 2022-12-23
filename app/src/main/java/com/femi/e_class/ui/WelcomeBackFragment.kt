package com.femi.e_class.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.femi.e_class.R
import com.femi.e_class.compose.E_ClassTheme
import com.femi.e_class.data.OnBoardingData
import com.femi.e_class.databinding.FragmentWelcomeBackBinding
import com.femi.e_class.viewmodels.BaseViewModel
import com.femi.e_class.viewmodels.HomeActivityViewModel
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
                        var userNameText by remember { mutableStateOf("") }
                        var greetingText by remember { mutableStateOf("") }
                        var isInClass by remember { mutableStateOf(false) }
                        val context = LocalContext.current
                        LaunchedEffect(key1 = true) {
                            userNameText = "Hi, ${viewModel.userFName()}"

                            greetingText = "Good Day"
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                val calendar = Calendar.getInstance()
                                val current = LocalDateTime.of(
                                    calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH),
                                    calendar.get(Calendar.HOUR_OF_DAY),
                                    calendar.get(Calendar.MINUTE),
                                    calendar.get(Calendar.SECOND))
                                greetingText = when (current.hour) {
                                    in 6..11 -> "Good Morning"
                                    in 12..16 -> "Good Afternoon"
                                    in 17..22 -> "Good Evening"
                                    else -> "You should be sleeping"
                                }
                            }

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
                        LaunchedEffect(key1 = context) {
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
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top,
                            modifier = Modifier
                                .verticalScroll(scrollState)
                                .fillMaxSize()
                                .padding(0.dp, 0.dp, 0.dp, 30.dp)
                        ) {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(30.dp, 0.dp, 30.dp, 0.dp),
                                text = userNameText,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Start
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            InspirationTextCard(
                                painter = painterResource(id = R.drawable.cover_image),
                                contentDescription = "Inspirational Text card",
                                text1 = "Let’s Learn\n" +
                                        "Now!",
                                text2 = "Start your classes on the go.",
                                onClick = {

                                }
                            )
                            if (isInClass) {
                                OnGoingClassCard(
                                    title = "Click the meeting notification to rejoin the class screen",
                                    onClick = {
                                        if (!(activity as HomeActivity).checkNotificationPermission()) {
                                            Toast.makeText(context,
                                                "Please enable notifications in settings",
                                                Toast.LENGTH_LONG).show()
                                        }

                                    }
                                )
                            }
                            Text(
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(30.dp, 0.dp, 30.dp, 0.dp),
                                text = greetingText,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Start
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            ImageCard(
                                painter = painterResource(id = R.drawable.start_or_join),
                                contentDescription = "Start or join a class card",
                                title = "Start or join a class",
                                onClick = {
                                    findNavController().navigate(R.id.action_welcomeBack_to_classDetails)
                                }
                            )
                            ImageCard(
                                painter = painterResource(id = R.drawable.update_profile),
                                contentDescription = "My profile card",
                                title = "My Profile",
                                onClick = {
                                    findNavController().navigate(R.id.action_welcomeBack_to_updateProfile)
                                }
                            )
                            ImageCard(
                                painter = painterResource(id = R.drawable.settings),
                                contentDescription = "Settings card",
                                title = "Settings",
                                onClick = {
                                    findNavController().navigate(R.id.action_welcomeBack_to_settings)
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
    fun InspirationTextCard(
        painter: Painter,
        contentDescription: String,
        text1: String,
        text2: String,
        modifier: Modifier = Modifier,
        onClick: () -> Unit,
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 22.dp, end = 22.dp),
            shape = AbsoluteRoundedCornerShape(24.dp),
            onClick = onClick,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 30.dp),
        ) {
            Row(modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.53f)
                        .padding(start = 22.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text =
                        buildAnnotatedString {
                            append(text1)
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            ) {
                                append("\n$text2")
                            }
                        },
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Start
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(1f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Image(
                        painter = painter,
                        contentDescription = contentDescription,
                        contentScale = ContentScale.FillWidth
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
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
                .padding(start = 22.dp, end = 22.dp),
            shape = AbsoluteRoundedCornerShape(24.dp),
            onClick = onClick,
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        ) {
            Box(modifier = Modifier
                .height(250.dp)
                .fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier
                        .height(250.dp)
                        .padding(0.dp)
                        .align(Alignment.TopCenter),
                    shape = AbsoluteRoundedCornerShape(24.dp),
                ) {
                    Image(
                        painter = painter,
                        contentDescription = contentDescription,
                        contentScale = ContentScale.Crop
                    )
                }
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(51, 51, 51, 255)
                        ),
                        startY = 500f
                    ))) {
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 20.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        style = TextStyle(color = Color(255, 255, 255, 255))
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun OnGoingClassCard(
        title: String,
        modifier: Modifier = Modifier,
        onClick: () -> Unit,
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth(),
            shape = AbsoluteCutCornerShape(0.dp),
            onClick = onClick,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        style = TextStyle(color = MaterialTheme.colorScheme.onSecondaryContainer),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class ImageCardItems(
        imageRes: Int,
        contentDescription: String,
        title: String,
        onClick: () -> Unit,
    )
}