package com.femi.e_class.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.femi.e_class.navigation.Screen

object BottomNavBarData {
    fun getItems(): List<BottomBarItem> {
        return listOf(
            BottomBarItem(
                name = "Home",
                route = Screen.HomeScreen.route,
                icon = Icons.Default.Home
            ),
            BottomBarItem(
                name = "Profile",
                route = Screen.ProfileScreen.route,
                icon = Icons.Default.Person
            ),
            BottomBarItem(
                name = "Settings",
                route = Screen.SettingsScreen.route,
                icon = Icons.Default.Settings
            ))
    }

    data class BottomBarItem(
        val name: String,
        val route: String,
        val icon: ImageVector
    )
}