package com.kotlin.easyrent.features.expenseTracking.domain.usecase

import com.kotlin.easyrent.features.expenseTracking.domain.repository.ExpensesRepository
import javax.inject.Inject

class GetRentalExpensesUseCase @Inject constructor(
    private val expensesRepository: ExpensesRepository
) {
    suspend operator fun invoke(rentalId: String) = expensesRepository.getRentalExpenses(rentalId)
}