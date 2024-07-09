package com.kotlin.easyrent.features.tenantManagement.domain.modal

import java.util.Date

data class Tenant(
    val id: String,
    val name: String,
    val phone: String,
    val email: String?,
    val balance: Double,
    val rentalId: String,
    val rentalName: String,
    val moveInDate: Long = Date().time,
    val address: String? = null,
    val profilePhotoUrl: String? = null,
    val description: String? = null,
    val isSynced: Boolean = false,
    val isDeleted: Boolean = false
) {
    constructor() : this(
        id = "",
        name = "",
        phone = "",
        email = "",
        balance = 0.0,
        rentalId = "",
        rentalName = ""
    )
}
