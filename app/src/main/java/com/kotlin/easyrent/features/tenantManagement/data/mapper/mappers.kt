package com.kotlin.easyrent.features.tenantManagement.data.mapper

import com.kotlin.easyrent.features.tenantManagement.data.modal.TenantEntity
import com.kotlin.easyrent.features.tenantManagement.domain.modal.Tenant

fun Tenant.toEntity() : TenantEntity {
    return TenantEntity(
        id = id,
        name = name,
        email = email,
        phone = phone,
        address = address,
        balance = balance,
        rentalId = rentalId,
        rentalName = rentalName,
        moveInDate = moveInDate,
        profilePhotoUrl = profilePhotoUrl,
        description = description,
        isSynced = isSynced,
        isDeleted = isDeleted
    )
}

fun TenantEntity.toDomain() : Tenant {
    return Tenant(
        id = id,
        name = name,
        email = email,
        phone = phone,
        address = address,
        balance = balance,
        rentalId = rentalId,
        rentalName = rentalName,
        moveInDate = moveInDate,
        profilePhotoUrl = profilePhotoUrl,
        description = description,
        isSynced = isSynced,
        isDeleted = isDeleted
    )
}