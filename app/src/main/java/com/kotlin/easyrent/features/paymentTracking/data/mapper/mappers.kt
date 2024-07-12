package com.kotlin.easyrent.features.paymentTracking.data.mapper

import com.kotlin.easyrent.features.paymentTracking.data.modal.PaymentEntity
import com.kotlin.easyrent.features.paymentTracking.domain.modal.Payment

fun Payment.toEntity() : PaymentEntity {
    return PaymentEntity(
        id = id,
        tenantId = tenantId,
        rentalId = rentalId,
        by = by,
        rentalName = rentalName,
        amount = amount,
        date = date,
        completed = completed,
        isSynced = isSynced,
        isDeleted = isDeleted
    )
}


fun PaymentEntity.toDomain() : Payment {
    return Payment(
        id = id,
        tenantId = tenantId,
        rentalId = rentalId,
        by = by,
        rentalName = rentalName,
        amount = amount,
        date = date,
        completed = completed,
        isSynced = isSynced,
        isDeleted = isDeleted
    )
}