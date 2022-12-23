package com.femi.e_class.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.femi.e_class.R
import com.femi.e_class.compose.E_ClassTheme
import com.femi.e_class.data.OnBoardingData
import com.femi.e_class.data.UserPreferences
import com.femi.e_class.databinding.ActivityMainBinding
import com.femi.e_class.repositories.MainActivityRepository
import com.femi.e_class.viewmodels.MainActivityViewModel
import com.femi.e_class.viewmodels.ViewModelFactory
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.PagerState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            this.setKeepOnScreenCondition {
                viewModel.isLoading
            }
        }
        setupViewModel()
        lifecycleScope.launch {
            returningUserCheck()
        }
        binding = ActivityMainBinding.inflate(layoutInflater)

        val onBoardingData = mutableStateOf(
                listOf(
                    OnBoardingData(R.drawable.telecommuting_bro,
                        "Join a Class",
                        "Join a class from the comfort of your home"),
                    OnBoardingData(R.drawable.telecommuting_rafiki,
                        "HD Audio-Video",
                        "Enjoy high quality audio paired with high definition video"),
                    OnBoardingData(R.drawable.typing_rafiki,
                        "Share Chat and screen",
                        "Share your screen and messages while in a class")
                )
            )

        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                E_ClassTheme(dynamicColor = viewModel.useDynamicTheme) {
                    Surface {
                        val scrollState = rememberScrollState()
                        val coroutineScope = rememberCoroutineScope()
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .verticalScroll(scrollState)
                                .fillMaxWidth()
                                .padding(8.dp, 0.dp, 8.dp, 0.dp),
                        ) {
                            val pagerState = rememberPageState()
                            OnBoardingViewPager(
                                item = onBoardingData.value,
                                pagerState = pagerState
                            )
                            HorizontalPagerIndicator(
                                modifier = Modifier
                                    .padding(0.dp, 20.dp, 0.dp, 20.dp)
                                    .align(Alignment.CenterHorizontally),
                                pagerState = pagerState,
                                inactiveColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                activeColor = MaterialTheme.colorScheme.primaryContainer,
                                indicatorHeight = 8.dp,
                                indicatorWidth = 12.dp,
                                spacing = 12.dp
                            )
                            AnimatedVisibility(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(40.dp, 40.dp, 40.dp, 0.dp),
                                visible = pagerState.currentPage == 2
                            ) {
                                Button(
                                    onClick = {
                                        startActivity(Intent(this@MainActivity,
                                            LoginActivity::class.java))
                                    }
                                ) {
                                    Text(text = "Log In")
                                }

                            }
                            AnimatedVisibility(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(40.dp, 20.dp, 40.dp, 0.dp),
                                visible = pagerState.currentPage == 2,
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        startActivity(Intent(this@MainActivity,
                                            SignUpActivity::class.java))
                                    }
                                ) {
                                    Text(text = "Sign Up")
                                }
                            }
                            AnimatedVisibility(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(40.dp, 60.dp, 40.dp, 0.dp),
                                visible = pagerState.currentPage != 2
                            ) {
                                Button(
                                    onClick = {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(
                                                page = pagerState.currentPage + 1
                                            )
                                        }
                                    }
                                ) {
                                    Text(text = "Next")
                                }
                            }
                        }
                    }
                }
            }
        }

        setContentView(binding.root)

    }


    @OptIn(ExperimentalPagerApi::class)
    @Composable
    private fun OnBoardingViewPager(
        item: List<OnBoardingData>,
        pagerState: PagerState,
        modifier: Modifier = Modifier,
    ) {
        Box(modifier = modifier) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalPager(
                    state = pagerState,
                    count = item.count()
                ) { page ->
                    Column(modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.4f),
                            contentScale = ContentScale.FillWidth,
                            painter = painterResource(id = item[page].image),
                            contentDescription = item[page].title
                        )
                        Text(modifier = Modifier
                            .fillMaxWidth(),
                            text = item[page].title,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center
                        )
                        Text(modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 40.dp)
                            .padding(top = 10.dp),
                            text = item[page].description,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }


    @OptIn(ExperimentalPagerApi::class)
    @Composable
    private fun rememberPageState(
        @androidx.annotation.IntRange(from = 0) initialPage: Int = 0,
    ): PagerState = rememberSaveable(saver = PagerState.Saver) {
        PagerState(
            currentPage = initialPage,
        )
    }

    private fun setupViewModel() {
        val dataStore = UserPreferences(this)
        val repository = MainActivityRepository(dataStore)
        val viewModelFactory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainActivityViewModel::class.java]
    }

    private suspend fun returningUserCheck() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val email = viewModel.userEmail()
        if (currentUser != null &&
            email.isNotEmpty() &&
            currentUser.email == email &&
            viewModel.userFName().isNotEmpty() &&
            viewModel.userLName().isNotEmpty() &&
            viewModel.userMatric() != 0L
        ) {
            Intent(this@MainActivity, HomeActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
                finish()
            }
        }
    }

}