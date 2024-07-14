package com.kotlin.easyrent.features.expenseTracking.ui.screens.expenses

import com.kotlin.easyrent.features.expenseTracking.domain.modal.Expense
import com.kotlin.easyrent.features.expenseTracking.domain.modal.ExpenseRange

sealed class ExpensesUiEvents {
    data object ExpenseDeleted: ExpensesUiEvents()
    data object DismissedDeleteDialog: ExpensesUiEvents()
    data class ExpenseSelected(val expense: Expense) : ExpensesUiEvents()

    data class SelectedExpenseRange(val range: ExpenseRange) : ExpensesUiEvents()
}