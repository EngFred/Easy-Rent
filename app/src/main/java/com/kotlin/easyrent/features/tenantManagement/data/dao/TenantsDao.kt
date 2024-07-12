package com.kotlin.easyrent.features.tenantManagement.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kotlin.easyrent.features.tenantManagement.data.modal.TenantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TenantsDao {
    @Query("SELECT * FROM tenants WHERE isDeleted = 0 ORDER BY id ASC")
    fun getAllTenants(): Flow<List<TenantEntity>>

    @Query("SELECT * FROM tenants WHERE isSynced = 0")
    suspend fun getAllUnsyncedTenants(): List<TenantEntity>

    @Query("SELECT * FROM tenants WHERE isDeleted = 1")
    suspend fun getAllDeletedTenants(): List<TenantEntity>

    @Query("SELECT * FROM tenants WHERE id = :id")
    suspend fun getTenantById(id: String): TenantEntity?

    @Query("SELECT * FROM tenants WHERE rentalId = :rentalId")
    suspend fun getAllTenantsForRental(rentalId: String): List<TenantEntity>

    @Query("DELETE FROM tenants WHERE id = :id")
    suspend fun deleteTenantById(id: String)

    @Upsert
    suspend fun upsertTenant(tenant: TenantEntity)

}