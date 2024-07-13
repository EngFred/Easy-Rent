package com.kotlin.easyrent.features.expenseTracking.data.mapper

import com.kotlin.easyrent.features.expenseTracking.data.modal.ExpenseEntity
import com.kotlin.easyrent.features.expenseTracking.domain.modal.Expense

fun Expense.toEntity() : ExpenseEntity {
    return  ExpenseEntity(
        id = id,
        rentalId = rentalId,
        rentalName = rentalName,
        description = description,
        amount = amount,
        date = date,
        isDeleted = isDeleted,
        isSynced = isSynced
    )
}

fun ExpenseEntity.toDomain() : Expense {
    return  Expense(
        id = id,
        rentalId = rentalId,
        rentalName = rentalName,
        description = description,
        amount = amount,
        date = date,
        isDeleted = isDeleted,
        isSynced = isSynced
    )
}