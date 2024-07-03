package com.kotlin.easyrent.core.routes

sealed class HomeRoutes(val destination: String ) {
    data object Expenses : HomeRoutes(destination = "expenses")
    data object Payments : HomeRoutes(destination = "payments")
    data object Rentals : HomeRoutes(destination = "rentals")
    data object Tenants : HomeRoutes(destination = "tenants")
    data object Dashboard : HomeRoutes(destination = "Dashboard")
    data object Profile : HomeRoutes(destination = "Profile")
}