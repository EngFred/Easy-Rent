package com.kotlin.easyrent.features.expenseTracking.domain.repository

import com.kotlin.easyrent.features.expenseTracking.domain.modal.Expense
import com.kotlin.easyrent.utils.ServiceResponse
import kotlinx.coroutines.flow.Flow

interface ExpensesRepository {
    suspend fun addExpense(expense: Expense) : ServiceResponse<Unit>
    fun getAllExpenses() : Flow<ServiceResponse<List<Expense>>>
    suspend fun deleteExpense(expense: Expense) : ServiceResponse<Unit>
    suspend fun getRentalExpenses(rentalId: String) : ServiceResponse<List<Expense>>
    suspend fun getAllUnsyncedExpenses() : ServiceResponse<List<Expense>>
    suspend fun getAllDeletedExpenses() : ServiceResponse<List<Expense>>
    suspend fun getExpenseById(expenseId: String) : ServiceResponse<Expense?>
    suspend fun deleteAllExpenses() : ServiceResponse<Unit>
    fun getTotalExpenses() : Flow<ServiceResponse<Double>>

}