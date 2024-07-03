package com.kotlin.easyrent.core.graphs

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kotlin.easyrent.core.presentation.SharedViewModel
import com.kotlin.easyrent.core.routes.AuthRoutes
import com.kotlin.easyrent.core.routes.Graphs
import com.kotlin.easyrent.features.auth.ui.screens.login.LoginScreen
import com.kotlin.easyrent.features.auth.ui.screens.signup.Signup

fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    modifier: Modifier,
    sharedViewModel: SharedViewModel
) {
    navigation(
        startDestination = AuthRoutes.Login.destination,
        route = Graphs.AUTH
    ) {

        composable(
            route = AuthRoutes.Login.destination
        ) {
            LoginScreen(onHomeClick = {
                navController.navigate(Graphs.HOME){
                    launchSingleTop = true
                    popUpTo(Graphs.AUTH){
                        inclusive = true
                    }
                }
            }, onSignupClick = {
                navController.navigate(AuthRoutes.Signup.destination) {
                    launchSingleTop = true
                    popUpTo(Graphs.AUTH) {
                        inclusive = true
                    }
                }
            })
        }

        composable(AuthRoutes.Signup.destination) {
            Signup(onHomeClick = {
                navController.navigate(Graphs.HOME){
                    launchSingleTop = true
                    popUpTo(Graphs.AUTH){
                        inclusive = true
                    }
                }
            }, onLoginClick = {
                navController.navigate(AuthRoutes.Login.destination) {
                    launchSingleTop = true
                    popUpTo(Graphs.AUTH) {
                        inclusive = true
                    }
                }
            })
        }

    }
}