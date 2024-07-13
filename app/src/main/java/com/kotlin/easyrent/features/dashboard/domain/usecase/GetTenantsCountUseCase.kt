package com.kotlin.easyrent.features.dashboard.domain.usecase

import com.kotlin.easyrent.features.dashboard.domain.repository.DashboardRepository
import javax.inject.Inject

class GetTenantsCountUseCase @Inject constructor(
    private val dashboardRepository: DashboardRepository
) {
    operator fun invoke() = dashboardRepository.getTotalTenants()
}