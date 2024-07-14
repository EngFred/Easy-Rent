package com.kotlin.easyrent.features.expenseTracking.ui.screens.expenses

import androidx.annotation.StringRes
import com.kotlin.easyrent.features.expenseTracking.domain.modal.Expense
import com.kotlin.easyrent.features.expenseTracking.domain.modal.ExpenseRange

data class ExpensesUIState(
    val isLoading: Boolean = true,
    @StringRes
    val loadError: Int? = null,

    val expense: Expense? = null,

    val allExpenses: List<Expense> = emptyList(),
    val filteredExpenses: List<Expense> = emptyList(),
    val totalExpenses: Double = 0.0,
    val selectedExpenseRange: ExpenseRange = ExpenseRange.Daily,

    val showDeleteExpenseDialog: Boolean = false,
    val deletingExpense: Boolean = false,
    @StringRes
    val deleteError: Int? = null,
    val deleteSuccessful: Boolean = false,
)
