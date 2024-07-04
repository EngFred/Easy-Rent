package com.kotlin.easyrent.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kotlin.easyrent.R
import com.kotlin.easyrent.core.presentation.SharedViewModel
import com.kotlin.easyrent.core.routes.HomeRoutes
import com.kotlin.easyrent.core.theme.poppinsBold
import com.kotlin.easyrent.home.bottomBar.BottomBar
import com.kotlin.easyrent.home.drawer.DrawerContent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    sharedViewModel: SharedViewModel,
    navController: NavHostController = rememberNavController()
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: HomeRoutes.Dashboard.destination

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                route = currentRoute,
                navigateToHome = {
                    navController.navigate(HomeRoutes.Dashboard.destination) {
                        popUpTo(navController.graph.findStartDestination().id){ saveState =  true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                navigateToTenants = {
                    navController.navigate(HomeRoutes.Tenants.destination) {
                        popUpTo(navController.graph.findStartDestination().id){ saveState =  true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                navigateToPayments = {
                    navController.navigate(HomeRoutes.Payments.destination) {
                        popUpTo(navController.graph.findStartDestination().id){ saveState =  true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                navigateToExpenses = {
                    navController.navigate(HomeRoutes.Expenses.destination) {
                        popUpTo(navController.graph.findStartDestination().id){ saveState =  true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                navigateToRentals = {
                    navController.navigate(HomeRoutes.Rentals.destination) {
                        popUpTo(navController.graph.findStartDestination().id){ saveState =  true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }, closeDrawer = {
                    coroutineScope.launch { drawerState.close() }
                })
        }
    ) {
        val title = when (currentRoute) {
            HomeRoutes.Tenants.destination -> stringResource(id = R.string.tenants)
            HomeRoutes.Payments.destination -> stringResource(id = R.string.payments)
            HomeRoutes.Expenses.destination -> stringResource(id = R.string.expenses)
            HomeRoutes.Rentals.destination -> stringResource(id = R.string.rentals)
            else -> "EasyRent"
        }
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text(text = title, fontFamily = poppinsBold) },
                    navigationIcon = {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(imageVector = Icons.Rounded.Menu, contentDescription = null )
                        }
                    }
                )
            },
            bottomBar = { BottomBar(navController = navController) }
        ) { innerPadding ->
            HomeNavGraph(
                navController = navController,
                sharedViewModel = sharedViewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}




