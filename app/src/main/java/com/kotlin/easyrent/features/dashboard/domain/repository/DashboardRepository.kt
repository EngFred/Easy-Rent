package com.kotlin.easyrent.features.dashboard.domain.repository

import com.kotlin.easyrent.utils.ServiceResponse
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {
    fun getTotalPayments() : Flow<ServiceResponse<Double>>
    fun getExpectedRevenue() : Flow<ServiceResponse<Double>>
    fun getTotalExpenses() : Flow<ServiceResponse<Double>>
    fun getTotalRentals() : Flow<ServiceResponse<Int>>
    fun getTotalTenants() : Flow<ServiceResponse<Int>>
}