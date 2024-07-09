package com.kotlin.easyrent.features.tenantManagement.domain.repository

import com.kotlin.easyrent.features.rentalManagement.domain.modal.Rental
import com.kotlin.easyrent.features.tenantManagement.domain.modal.Tenant
import com.kotlin.easyrent.features.tenantManagement.domain.modal.TenantStatus
import com.kotlin.easyrent.utils.ServiceResponse
import kotlinx.coroutines.flow.Flow

interface TenantsRepository {
    suspend fun upsertTenant(tenant: Tenant, rental: Rental, tenantStatus: TenantStatus) : ServiceResponse<Unit>
    fun getAllTenants() : Flow<ServiceResponse<List<Tenant>>>
    fun getAllUnsyncedTenants() : Flow<ServiceResponse<List<Tenant>>>
    fun getAllDeletedTenants() : Flow<ServiceResponse<List<Tenant>>>
    suspend fun deleteTenant(tenant: Tenant, rental: Rental) : ServiceResponse<Unit>
    suspend fun getTenantById(id: String) : ServiceResponse<Tenant?>
}