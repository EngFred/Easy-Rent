package com.kotlin.easyrent.features.expenseTracking.data.modal

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey
    val id: String,
    val rentalId: String,
    val rentalName: String,
    val amount: Double,
    val description: String,
    val date: Long = Date().time,
    val isSynced: Boolean = false,
    val isDeleted: Boolean = false
)