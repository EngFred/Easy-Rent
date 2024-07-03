package com.kotlin.easyrent.core.graphs

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kotlin.easyrent.core.presentation.SharedViewModel
import com.kotlin.easyrent.core.routes.HomeRoutes
import com.kotlin.easyrent.core.routes.Graphs
import com.kotlin.easyrent.features.expenseTracking.ui.screens.ExpensesScreen
import com.kotlin.easyrent.features.paymentTracking.ui.screens.PaymentsScreen
import com.kotlin.easyrent.features.rentalManagement.ui.screens.RentalsScreen
import com.kotlin.easyrent.features.tenantManagement.ui.screens.TenantsScreen

fun NavGraphBuilder.detailGraph(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    modifier: Modifier = Modifier
) {
    navigation(
        startDestination = HomeRoutes.Tenants.destination,
        route = Graphs.DETAILS
    ) {

        composable(
            route = HomeRoutes.Tenants.destination
        ) {
            TenantsScreen()
        }

        composable(
            route = HomeRoutes.Expenses.destination
        ) {
            ExpensesScreen()
        }

        composable(
            route = HomeRoutes.Payments.destination
        ) {
           PaymentsScreen()
        }

        composable(
            route = HomeRoutes.Rentals.destination
        ) {
            RentalsScreen()
        }

    }
}