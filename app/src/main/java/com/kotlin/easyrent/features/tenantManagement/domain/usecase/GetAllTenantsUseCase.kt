package com.kotlin.easyrent.features.tenantManagement.domain.usecase

import com.kotlin.easyrent.features.tenantManagement.domain.repository.TenantsRepository
import javax.inject.Inject

class GetAllTenantsUseCase @Inject constructor(
    private val tenantsRepository: TenantsRepository
) {
    operator fun invoke() = tenantsRepository.getAllTenants()
}