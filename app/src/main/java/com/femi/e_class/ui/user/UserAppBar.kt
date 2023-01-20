package com.femi.e_class.ui.user

import androidx.compose.material3.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.femi.e_class.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAppBar(title: String = "App Name") {
    TopAppBar(
        title = {
            Text(
                text = title,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(12.dp)
        )
    )
}