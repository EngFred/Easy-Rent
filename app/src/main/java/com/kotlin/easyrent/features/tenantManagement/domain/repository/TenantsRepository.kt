package com.kotlin.easyrent.features.tenantManagement.domain.repository

import com.kotlin.easyrent.features.rentalManagement.domain.modal.Rental
import com.kotlin.easyrent.features.tenantManagement.domain.modal.Tenant
import com.kotlin.easyrent.features.tenantManagement.domain.modal.TenantStatus
import com.kotlin.easyrent.utils.ServiceResponse
import kotlinx.coroutines.flow.Flow

interface TenantsRepository {
    suspend fun upsertTenant(tenant: Tenant, rental: Rental, tenantStatus: TenantStatus,
                             oldRentalId: String) : ServiceResponse<Unit>
    fun getAllTenants() : Flow<ServiceResponse<List<Tenant>>>
    suspend fun getAllUnsyncedTenants() : ServiceResponse<List<Tenant>>
    suspend fun getAllDeletedTenants() : ServiceResponse<List<Tenant>>
    suspend fun deleteTenant(tenant: Tenant, rental: Rental) : ServiceResponse<Unit>
    suspend fun getTenantById(id: String) : ServiceResponse<Tenant?>
}