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
    navController: NavHostController,
    startDestination: String
){

    NavHost(navController = navController, route = Graphs.ROOT, startDestination = startDestination) {
        authGraph(navController = navController, sharedViewModel = sharedViewModel)
        composable(Graphs.HOME) {
            HomeScreen(
                sharedViewModel = sharedViewModel,
                modifier = modifier,
                onLogout = {
                    navController.navigate(Graphs.AUTH){
                        popUpTo(Graphs.AUTH) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}