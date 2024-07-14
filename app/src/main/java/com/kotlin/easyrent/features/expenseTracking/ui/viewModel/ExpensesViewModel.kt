package com.kotlin.easyrent.features.expenseTracking.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.easyrent.features.expenseTracking.domain.modal.Expense
import com.kotlin.easyrent.features.expenseTracking.domain.modal.ExpenseRange
import com.kotlin.easyrent.features.expenseTracking.domain.usecase.DeleteExpenseUseCase
import com.kotlin.easyrent.features.expenseTracking.domain.usecase.GetAllExpensesUseCase
import com.kotlin.easyrent.features.expenseTracking.ui.screens.expenses.ExpensesUIState
import com.kotlin.easyrent.features.expenseTracking.ui.screens.expenses.ExpensesUiEvents
import com.kotlin.easyrent.utils.ServiceResponse
import com.kotlin.easyrent.utils.getEndOfDay
import com.kotlin.easyrent.utils.getEndOfMonth
import com.kotlin.easyrent.utils.getEndOfWeek
import com.kotlin.easyrent.utils.getEndOfYear
import com.kotlin.easyrent.utils.getStartOfDay
import com.kotlin.easyrent.utils.getStartOfMonth
import com.kotlin.easyrent.utils.getStartOfWeek
import com.kotlin.easyrent.utils.getStartOfYear
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val deleteExpenseUseCase: DeleteExpenseUseCase,
    private val getAllExpensesUseCase: GetAllExpensesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpensesUIState())
    val uiState = _uiState.asStateFlow()

    init {
        getAllExpenses()
    }

    fun onEvent(event: ExpensesUiEvents) {
        when(event) {
            ExpensesUiEvents.ExpenseDeleted -> { //2nd
                _uiState.update {
                    it.copy(
                        showDeleteExpenseDialog = false
                    )
                }
                _uiState.value.expense?.let {
                    deleteExpense(it)
                }
            }
            is ExpensesUiEvents.ExpenseSelected -> { //1st
                _uiState.update {
                    it.copy(
                        expense = event.expense,
                        showDeleteExpenseDialog =  true,
                        deleteSuccessful = false
                    )
                }
            }

            ExpensesUiEvents.DismissedDeleteDialog -> {
                _uiState.update {
                    it.copy(
                        showDeleteExpenseDialog = false
                    )
                }
            }

            is ExpensesUiEvents.SelectedExpenseRange -> {
                if ( _uiState.value.selectedExpenseRange != event.range ) {
                    _uiState.update {
                        it.copy(
                            selectedExpenseRange = event.range
                        )
                    }
                    when(event.range){
                        ExpenseRange.Daily -> {
                            val startDate = getStartOfDay()
                            val endDate = getEndOfDay()
                            filterExpensesWithinRange(startDate, endDate)
                        }
                        ExpenseRange.Weekly -> {
                            val startDate = getStartOfWeek()
                            val endDate = getEndOfWeek()
                            filterExpensesWithinRange(startDate, endDate)
                        }
                        ExpenseRange.Monthly -> {
                            val startDate = getStartOfMonth()
                            val endDate = getEndOfMonth()
                            filterExpensesWithinRange(startDate, endDate)
                        }
                        ExpenseRange.Yearly -> {
                            val startDate = getStartOfYear()
                            val endDate = getEndOfYear()
                            filterExpensesWithinRange(startDate, endDate)
                        }
                    }
                }
            }
        }
    }

    private fun getAllExpenses()  = viewModelScope.launch {
        getAllExpensesUseCase().collect { result ->
            when(result) {
                is ServiceResponse.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            loadError = result.message
                        )
                    }
                }
                ServiceResponse.Idle -> Unit
                is ServiceResponse.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            allExpenses = result.data
                        )
                    }
                    val startDate = getStartOfDay()
                    val endDate = getEndOfDay()
                    filterExpensesWithinRange(startDate, endDate)
                }
            }
        }
    }

    private fun filterExpensesWithinRange(startDate: Long, endDate: Long) {
        var totalExpense = 0.0
        val filteredExpense = _uiState.value.allExpenses.filter { expense ->
            expense.date in startDate..endDate
        }
        filteredExpense.forEach { expense ->
            totalExpense += expense.amount
        }
        _uiState.update {
            it.copy(
                filteredExpenses = filteredExpense,
                totalExpenses = totalExpense
            )
        }
    }

    private fun deleteExpense( expense: Expense ) = viewModelScope.launch(Dispatchers.IO) {
        _uiState.update {
            it.copy(
                deletingExpense = true,
                deleteError = null,
                showDeleteExpenseDialog = false
            )
        }
        val result = deleteExpenseUseCase.invoke(expense)
        Log.d("TAG", "Task done!")
        when(result) {
            is ServiceResponse.Error -> {
                _uiState.update {
                    it.copy(
                        deletingExpense = false,
                        deleteError = result.message
                    )
                }
            }
            ServiceResponse.Idle -> Unit
            is ServiceResponse.Success -> {
                _uiState.update {
                    it.copy(
                        deletingExpense = false,
                        deleteSuccessful = true
                    )
                }
            }
        }
    }
}