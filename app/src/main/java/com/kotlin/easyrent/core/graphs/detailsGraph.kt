package com.kotlin.easyrent.core.graphs

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.kotlin.easyrent.core.presentation.SharedViewModel
import com.kotlin.easyrent.core.routes.Graphs
import com.kotlin.easyrent.core.routes.HomeRoutes
import com.kotlin.easyrent.features.expenseTracking.ui.screens.ExpensesScreen
import com.kotlin.easyrent.features.paymentTracking.ui.screens.PaymentsScreen
import com.kotlin.easyrent.features.rentalManagement.ui.screens.rentals.RentalsScreen
import com.kotlin.easyrent.features.rentalManagement.ui.screens.upsert.UpsertRentalsScreen
import com.kotlin.easyrent.features.tenantManagement.ui.screens.TenantsScreen
import com.kotlin.easyrent.utils.Keys

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
            route = "${HomeRoutes.RentalUpsert.destination}?{${Keys.RENTAL_ID}}",
            arguments = listOf(
                navArgument(Keys.RENTAL_ID) {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { navBackStackEntry ->
            val rentalId = navBackStackEntry.arguments?.getString(Keys.RENTAL_ID)
            UpsertRentalsScreen(
                modifier = modifier,
                rentalId = rentalId,
                onUpsertSuccess = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = HomeRoutes.Rentals.destination
        ) {
            RentalsScreen(
                modifier = modifier,
                onAddRental = {
                    navController.navigate(
                        HomeRoutes.RentalUpsert.destination
                    )
                },
                onUpdateRental = { rentalId ->
                    navController.navigate(
                        "${HomeRoutes.RentalUpsert.destination}?$rentalId"
                    )
                }
            )
        }

    }
}

