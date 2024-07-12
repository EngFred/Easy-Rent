package com.kotlin.easyrent.features.paymentTracking.data.modal

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity("payments")
data class PaymentEntity(
    @PrimaryKey
    val id: String,
    val tenantId: String,
    val rentalId: String,
    val by: String,
    val rentalName: String,
    val amount: Double,
    val date: Long = Date().time,
    val completed: Boolean = false,
    val isSynced: Boolean = false,
    val isDeleted: Boolean = false
)