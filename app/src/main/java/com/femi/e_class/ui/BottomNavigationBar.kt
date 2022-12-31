package com.femi.e_class.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.femi.e_class.data.BottomNavBarData

@Composable
fun BottomNavigationBar(
    items: List<BottomNavBarData.BottomBarItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavBarData.BottomBarItem) -> Unit,
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    NavigationBar(
        modifier = modifier,
        tonalElevation = 6.dp,
        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(12.dp)
    ) {
        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            NavigationBarItem(
                selected = selected,
                onClick = {
//                    To close immediately
//                    navController.popBackStack(
//                        route = backStackEntry.value?.destination?.route
//                            ?: return@NavigationBarItem,
//                        inclusive = true
//                    )

                    navController.popBackStack(
                        route = item.route,
                        inclusive = true
                    )

                    onItemClick(item)
                },
                label = {
                    Text(
                        text = item.name,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = "${item.name} Icon"
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primary,
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                )
            )

        }
    }
}