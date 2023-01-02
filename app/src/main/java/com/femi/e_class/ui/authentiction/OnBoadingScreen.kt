package com.femi.e_class.ui.authentiction

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.femi.e_class.data.OnBoardingData
import com.femi.e_class.navigation.Screen
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoadingScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPageState()
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(8.dp, 0.dp, 8.dp, 0.dp),
        ) {

            OnBoardingViewPager(
                item = OnBoardingData.getItems(),
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
                        navController.navigate(Screen.LogInScreen.route)
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
                        navController.navigate(Screen.SignUpScreen.route)
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

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun OnBoardingViewPager(
    item: List<OnBoardingData.OnBoardingItem>,
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