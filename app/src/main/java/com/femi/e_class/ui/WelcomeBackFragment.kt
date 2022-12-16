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
import com.femi.e_class.viewmodels.HomeActivityViewModel
import kotlinx.coroutines.launch
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
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .verticalScroll(scrollState)
                                .fillMaxSize()
                                .padding(20.dp, 60.dp, 20.dp, 30.dp),
                        ) {
                            LaunchedEffect(key1 = Unit) {
                                lifecycleScope.launch {
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
                            }
                            Text(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally),
                                text = greetingText,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 24.sp,
                                textAlign = TextAlign.Start
                            )
                            Spacer(modifier = Modifier.height(40.dp))
                            ImageCard(
                                painter = painterResource(id = R.drawable.status_update_amico),
                                contentDescription = "Update profile card",
                                title = "Update Profile",
                                onClick = {
                                    findNavController().navigate(R.id.action_welcomeBack_to_updateProfile)
                                }
                            )
                            Spacer(modifier = Modifier.height(40.dp))
                            ImageCard(
                                painter = painterResource(id = R.drawable.online_learning_rafiki),
                                contentDescription = "Start or join a class card",
                                title = "Start of join a class",
                                onClick = {
                                    findNavController().navigate(R.id.action_welcomeBack_to_classDetails)
                                }
                            )
                            Spacer(modifier = Modifier.height(40.dp))
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
                .fillMaxWidth(),
            shape = AbsoluteCutCornerShape(15.dp),
            onClick = onClick
        ) {
            Box(modifier = Modifier
                .height(250.dp)
                .fillMaxWidth()
            ) {
                Image(
                    painter = painter,
                    contentDescription = contentDescription,
                    modifier = Modifier.align(Alignment.Center),
                    contentScale = ContentScale.FillHeight
                )
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.primaryContainer
                        ),
                        startY = 300f
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null && context != null) {


            /*

            lifecycleScope.launch {
                val greetingText = "${getString(R.string.welcome_back)} ${viewModel.userFName()}"
                binding.toolbarText.text = greetingText
                binding.toolbarLayout.title = "$greeting${viewModel.userFName()}"
                binding.appBarLayout.addOnOffsetChangedListener { _, verticalOffSet ->
                    if (abs(verticalOffSet) == binding.appBarLayout.totalScrollRange) {
                        //Collapsed
                        lifecycleScope.launch {
                            binding.toolbarLayout.title = "$greeting${viewModel.userFName()}"
                        }
                    } else {
                        //Expanded
                        lifecycleScope.launch {
                            binding.toolbarLayout.title = greeting
                        }
                    }
                }

            }


            binding.btnStartOrJoin.setOnClickListener {
                findNavController().navigate(R.id.action_welcomeBack_to_classDetails)
            }

            binding.btnUpdateProfile.setOnClickListener {
                findNavController().navigate(R.id.action_welcomeBack_to_updateProfile)
            }

            binding.btnMyAccount.setOnClickListener {
                findNavController().navigate(R.id.action_welcomeBack_to_my_account)
            }


             */
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}