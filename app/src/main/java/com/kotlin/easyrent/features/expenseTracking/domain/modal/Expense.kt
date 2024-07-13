package com.kotlin.easyrent.features.expenseTracking.domain.modal

import java.util.Date

data class Expense(
    val id: String,
    val rentalId: String,
    val rentalName: String,
    val amount: Double,
    val description: String,
    val date: Long = Date().time,
    val isSynced: Boolean = false,
    val isDeleted: Boolean = false
) {
    constructor() : this(
        "",
        "",
        "",
        0.0,
        ""
    )
}