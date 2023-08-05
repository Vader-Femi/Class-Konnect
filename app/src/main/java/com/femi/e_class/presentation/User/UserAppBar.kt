package com.femi.e_class.presentation.User

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.femi.e_class.data.BottomNavBarData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAppBar(
    appName: String,
    title: String
) {
    TopAppBar(
        title = {
            Text(
                text = if (title == BottomNavBarData.getItems()[0].name) appName else title,
            )
        },
//        modifier = Modifier
//            .background(
//                brush = Brush.verticalGradient(
//                    colors = listOf(
//                        MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp),
//                        MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
//                        Color.Transparent,
//                        Color.Transparent
//                    )
//                )
//            ),
        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = Color.Transparent,
        ),
    )
}