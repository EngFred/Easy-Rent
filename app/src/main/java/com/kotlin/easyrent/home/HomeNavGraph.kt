package com.kotlin.easyrent.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kotlin.easyrent.core.graphs.detailGraph
import com.kotlin.easyrent.core.presentation.SharedViewModel
import com.kotlin.easyrent.core.routes.Graphs
import com.kotlin.easyrent.core.routes.HomeRoutes
import com.kotlin.easyrent.features.dashboard.ui.screens.DashboardScreen
import com.kotlin.easyrent.features.profile.ui.screens.ProfileScreen

@Composable
fun HomeNavGraph(
    modifier: Modifier = Modifier,
    sharedViewModel: SharedViewModel,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        route = Graphs.HOME,
        startDestination = HomeRoutes.Dashboard.destination
    ) {
        composable(
            route = HomeRoutes.Dashboard.destination
        ) {
            DashboardScreen()
        }
        composable(
            route = HomeRoutes.Profile.destination
        ) {
            ProfileScreen( modifier = modifier, sharedViewModel = sharedViewModel )
        }
        detailGraph(navController = navController, modifier = modifier, sharedViewModel = sharedViewModel)
    }
}

