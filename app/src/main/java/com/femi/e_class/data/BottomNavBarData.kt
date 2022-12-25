package com.femi.e_class.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

object BottomNavBarData {
    fun getItems(): List<BottomBarItem> {
        return listOf(
            BottomBarItem(
                name = "Home",
                route = "home",
                icon = Icons.Default.Home
            ),
            BottomBarItem(
                name = "Profile",
                route = "profile",
                icon = Icons.Default.Person
            ),
            BottomBarItem(
                name = "Settings",
                route = "settings",
                icon = Icons.Default.Settings
            ))
    }

    data class BottomBarItem(
        val name: String,
        val route: String,
        val icon: ImageVector
    )
}