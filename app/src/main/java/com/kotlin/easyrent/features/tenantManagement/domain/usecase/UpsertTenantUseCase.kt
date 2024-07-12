package com.kotlin.easyrent.features.tenantManagement.domain.usecase

import com.kotlin.easyrent.features.rentalManagement.domain.modal.Rental
import com.kotlin.easyrent.features.tenantManagement.domain.modal.Tenant
import com.kotlin.easyrent.features.tenantManagement.domain.modal.TenantStatus
import com.kotlin.easyrent.features.tenantManagement.domain.repository.TenantsRepository
import javax.inject.Inject

class UpsertTenantUseCase @Inject constructor(
    private val tenantsRepository: TenantsRepository
) {
    suspend operator fun invoke(
        tenant: Tenant,
        rental: Rental,
        tenantStatus: TenantStatus,
        oldRentalId: String
    ) = tenantsRepository.upsertTenant(tenant, rental, tenantStatus, oldRentalId)
}