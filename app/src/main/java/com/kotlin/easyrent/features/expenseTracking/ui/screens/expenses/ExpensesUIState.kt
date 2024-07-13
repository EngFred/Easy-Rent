package com.kotlin.easyrent.features.expenseTracking.ui.screens.expenses

import androidx.annotation.StringRes
import com.kotlin.easyrent.features.expenseTracking.domain.modal.Expense

data class ExpensesUIState(
    val isLoading: Boolean = true,
    @StringRes
    val loadError: Int? = null,

    val expense: Expense? = null,

    val expenses: List<Expense> = emptyList(),

    val showDeleteExpenseDialog: Boolean = false,
    val deletingExpense: Boolean = false,
    @StringRes
    val deleteError: Int? = null,
    val deleteSuccessful: Boolean = false,
)
