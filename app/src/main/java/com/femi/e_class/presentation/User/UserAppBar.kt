package com.femi.e_class.presentation.User

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAppBar(title: String = "App Name") {
    TopAppBar(
        title = {
            Text(
                text = title,
            )
        },
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp),
                        MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
                        Color.Transparent,
                        Color.Transparent
                    )
                )
            ),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        )
    )
}