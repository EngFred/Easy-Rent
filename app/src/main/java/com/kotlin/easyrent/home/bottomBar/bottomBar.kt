package com.kotlin.easyrent.home.bottomBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kotlin.easyrent.R
import com.kotlin.easyrent.core.routes.HomeRoutes
import com.kotlin.easyrent.core.theme.poppins

@Composable
fun BottomBar(modifier: Modifier = Modifier, navController: NavHostController) {

    val bottomBarItemsLists = listOf(
        BottomBarItem(route = HomeRoutes.Dashboard.destination, icon = Icons.Default.Dashboard, label = stringResource(
            id = R.string.dashboard
        )),
        BottomBarItem(route = HomeRoutes.Profile.destination, icon = Icons.Default.Person, label = stringResource(
            id = R.string.profile
        ))
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = bottomBarItemsLists.any { it.route == currentDestination?.route }

    if (showBottomBar) {
        NavigationBar(
            containerColor = Color.Transparent,
        ) {
            bottomBarItemsLists.forEach{ navItem ->
                NavigationBarItem(
                    selected = navItem.route == currentDestination?.route,
                    onClick = {
                        navController.navigate(navItem.route) {
                            popUpTo(navController.graph.findStartDestination().id){ saveState =  true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(imageVector = navItem.icon, contentDescription = navItem.label) },
                    label = { Text(text = navItem.label, fontFamily = poppins, fontWeight = FontWeight.SemiBold) }
                )
            }
        }
    }

}