package com.kotlin.easyrent.features.paymentTracking.domain.modal

import java.util.Date

data class Payment(
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
) {
    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        0.0,
        0,
        false,
        false,
    )
}
