package com.kotlin.easyrent.features.tenantManagement.domain.usecase

import com.kotlin.easyrent.features.rentalManagement.domain.modal.Rental
import com.kotlin.easyrent.features.tenantManagement.domain.modal.Tenant
import com.kotlin.easyrent.features.tenantManagement.domain.repository.TenantsRepository
import javax.inject.Inject

class DeleteTenantUseCase @Inject constructor(
    private val tenantsRepository: TenantsRepository
) {
    suspend operator fun invoke(tenant: Tenant, rental: Rental) = tenantsRepository.deleteTenant(tenant, rental)
}