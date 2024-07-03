package com.kotlin.easyrent.core.graphs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kotlin.easyrent.core.presentation.SharedViewModel
import com.kotlin.easyrent.core.routes.Graphs
import com.kotlin.easyrent.home.HomeScreen

@Composable
fun RootGraph(
    modifier: Modifier = Modifier,
    sharedViewModel: SharedViewModel,
    navController: NavHostController
){

    NavHost(navController = navController, route = Graphs.ROOT, startDestination = Graphs.AUTH) {
        authGraph(navController, modifier, sharedViewModel)
        composable(Graphs.HOME) {
            HomeScreen(sharedViewModel = sharedViewModel)
        }
    }
}