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
import com.kotlin.easyrent.features.paymentTracking.ui.screens.payments.PaymentsScreen
import com.kotlin.easyrent.features.paymentTracking.ui.screens.upsert.PaymentUpsertScreen
import com.kotlin.easyrent.features.rentalManagement.ui.screens.rentals.RentalsScreen
import com.kotlin.easyrent.features.rentalManagement.ui.screens.upsert.UpsertRentalsScreen
import com.kotlin.easyrent.features.tenantManagement.ui.screens.tenants.TenantsScreen
import com.kotlin.easyrent.features.tenantManagement.ui.screens.upsert.UpsertTenantScreen
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
            TenantsScreen(
                modifier = modifier,
                onAddTenant = {
                    navController.navigate(
                        HomeRoutes.TenantUpsert.destination
                    ) {
                        launchSingleTop = true
                    }
                },
                onUpdateTenant = { tenantId ->
                    navController.navigate(
                        "${HomeRoutes.TenantUpsert.destination}?$tenantId"
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = HomeRoutes.Expenses.destination
        ) {
            ExpensesScreen()
        }

        composable(
            route = HomeRoutes.Payments.destination
        ) {
           PaymentsScreen(
               modifier = modifier,
               onAddPayment = {
                   navController.navigate(
                       HomeRoutes.PaymentUpsert.destination
                   ) {
                       launchSingleTop = true
                   }
               },
               onUpsertPayment = { paymentId ->
                   navController.navigate(
                       "${HomeRoutes.PaymentUpsert.destination}?$paymentId"
                   ) {
                       launchSingleTop = true
                   }
               }
           )
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
                onTaskSuccess = {
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


        composable(
            route = "${HomeRoutes.TenantUpsert.destination}?{${Keys.TENANT_ID}}",
            arguments = listOf(
                navArgument(Keys.TENANT_ID) {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { navBackStackEntry ->
            val tenantId = navBackStackEntry.arguments?.getString(Keys.TENANT_ID)
            UpsertTenantScreen(
                modifier = modifier,
                tenantId = tenantId,
                onTaskSuccess = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "${HomeRoutes.PaymentUpsert.destination}?{${Keys.PAYMENT_ID}}",
            arguments = listOf(
                navArgument(Keys.PAYMENT_ID) {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { navBackStackEntry ->
            val paymentId = navBackStackEntry.arguments?.getString(Keys.PAYMENT_ID)
            PaymentUpsertScreen(
                modifier = modifier,
                paymentId = paymentId,
                onTaskSuccess = {
                    navController.popBackStack()
                }
            )
        }

    }
}

