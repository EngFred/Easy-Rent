package com.kotlin.easyrent.core.routes

sealed class HomeRoutes(val destination: String ) {
    data object Expenses : HomeRoutes(destination = "expenses")
    data object AddExpense : HomeRoutes(destination = "add_expense")
    data object Payments : HomeRoutes(destination = "payments")
    data object AddPayment : HomeRoutes(destination = "add_payment")
    data object Rentals : HomeRoutes(destination = "rentals")

    data object RentalUpsert : HomeRoutes(destination = "rental_upsert")
    data object Tenants : HomeRoutes(destination = "tenants")
    data object TenantUpsert : HomeRoutes(destination = "tenants_upsert")
    data object Dashboard : HomeRoutes(destination = "Dashboard")
    data object Profile : HomeRoutes(destination = "Profile")
}
