package com.kotlin.easyrent.features.tenantManagement.data.modal

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tenants")
data class TenantEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val phone: String,
    val email: String?,
    val balance: Double,
    val rentalId: String,
    val rentalName: String,
    val moveInDate: Long = Date().time,
    val unpaidMonths: Int = 0,
    val lastResetDate: Long = Date().time,
    val address: String? = null,
    val profilePhotoUrl: String? = null,
    val description: String? = null,
    val isSynced: Boolean = false,
    val isDeleted: Boolean = false
)
