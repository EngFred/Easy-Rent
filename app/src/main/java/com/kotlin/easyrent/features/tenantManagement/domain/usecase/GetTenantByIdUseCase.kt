package com.kotlin.easyrent.features.tenantManagement.domain.usecase

import com.kotlin.easyrent.features.tenantManagement.domain.repository.TenantsRepository
import javax.inject.Inject

class GetTenantByIdUseCase @Inject constructor(
    private val tenantsRepository: TenantsRepository
) {
    suspend operator fun invoke(tenantId: String) = tenantsRepository.getTenantById(tenantId)
}