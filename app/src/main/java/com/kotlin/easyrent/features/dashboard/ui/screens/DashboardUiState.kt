package com.kotlin.easyrent.features.dashboard.ui.screens

data class DashboardUiState(
    val totalExpenses: Double = 0.0,
    val totalPayments: Double = 0.0,
    val profits: Double = 0.0,
    val expectedRevenue: Double = 0.0,
    val totalTenants: Int = 0,
    val totalRentals: Int = 0,
)
