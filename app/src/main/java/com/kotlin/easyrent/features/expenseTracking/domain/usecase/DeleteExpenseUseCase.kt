package com.kotlin.easyrent.features.expenseTracking.domain.usecase

import com.kotlin.easyrent.features.expenseTracking.domain.modal.Expense
import com.kotlin.easyrent.features.expenseTracking.domain.repository.ExpensesRepository
import javax.inject.Inject

class DeleteExpenseUseCase @Inject constructor(
    private val expensesRepository: ExpensesRepository
) {
    suspend operator fun invoke(expense: Expense) = expensesRepository.deleteExpense(expense)
}