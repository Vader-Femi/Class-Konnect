package com.femi.e_class.ui

import android.content.Intent
import android.os.Bundle
import androidx.annotation.FloatRange
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.google.accompanist.pager.PagerState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var viewPagerAdapter: OnBoardingViewPagerAdapter

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

        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                E_ClassTheme(dynamicColor = viewModel.useDynamicTheme) {
                    Surface {
                        val context = LocalContext.current
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp, 0.dp, 8.dp, 0.dp),
                        ) {

                            val onBoardingData = arrayListOf<OnBoardingData>()
                            onBoardingData.add(OnBoardingData(R.drawable.telecommuting_bro,
                                "Join a Class",
                                "Join a class from the comfort of your home"))
                            onBoardingData.add(OnBoardingData(R.drawable.telecommuting_pana,
                                "Join a Class2",
                                "Join a class from the comfort of your home"))
                            onBoardingData.add(OnBoardingData(R.drawable.telecommuting_rafiki,
                                "Join a Class3",
                                "Join a class from the comfort of your home"))

                            val pagerState = rememberPageState()
                            OnBoardingViewPager(
                                item = onBoardingData,
                                pagerState = pagerState,
                                onClickLogin = {
                                    startActivity(Intent(this@MainActivity,
                                        LoginActivity::class.java))
                                    finish()
                                },
                                onClickSignUp = {
                                    startActivity(Intent(this@MainActivity,
                                        SignUpActivity::class.java))
                                    finish()
                                })

                        }
                    }
                }
            }
        }

        setContentView(binding.root)

//        setSupportActionBar(binding.toolbar)
//
//        setupViewPager()
//
//        binding.btnSignUp.setOnClickListener {
//            startActivity(Intent(this, SignUpActivity::class.java))
//        }
//
//        binding.btnSignIn.setOnClickListener {
//            startActivity(Intent(this, LoginActivity::class.java))
//        }


    }


    @OptIn(ExperimentalPagerApi::class)
    @Composable
    private fun OnBoardingViewPager(
        item: List<OnBoardingData>,
        pagerState: PagerState,
        modifier: Modifier = Modifier,
        onClickLogin: () -> Unit,
        onClickSignUp: () -> Unit,
    ) {
        Box(modifier = modifier) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                HorizontalPager(
                    state = pagerState,
                    count = item.count()
                ) { page ->
                    Column(modifier = Modifier
                        .padding(top = 60.dp)
                        .fillMaxWidth(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.4f),
                            painter = painterResource(id = item[page].image),
                            contentDescription = item[page].title)
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
                        AnimatedVisibility(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp, 40.dp, 40.dp, 0.dp),
                            visible = pagerState.currentPage == 2
                        ) {
                            Button(
                                onClick = onClickLogin
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
                                onClick = onClickSignUp
                            ) {
                                Text(text = "Sign Up")
                            }

                        }
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
/*
    private fun setupViewPager() {
        viewPagerAdapter = OnBoardingViewPagerAdapter(this)
        binding.viewPager.adapter = viewPagerAdapter
        binding.dotIndicator.attachTo(binding.viewPager)


        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        binding.tvOnBoarding.text = "Start your Online Class Anywhere and more shalaye text sha... blah blah"
                    }
                    1 -> {
                        binding.tvOnBoarding.text = "Start your Online Class Anywhere and more shalaye text sha... blah blah"
                    }
                    2 -> {
                        binding.tvOnBoarding.text = "Start your Online Class Anywhere and more shalaye text sha... blah blah"
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
    }
    */

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