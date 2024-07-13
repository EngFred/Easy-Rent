package com.kotlin.easyrent.features.expenseTracking.domain.usecase

import com.kotlin.easyrent.features.expenseTracking.domain.repository.ExpensesRepository
import javax.inject.Inject

class GetTotalExpensesUseCase @Inject constructor(
    private val expensesRepository: ExpensesRepository
) {
    operator fun invoke()  = expensesRepository.getTotalExpenses()
}