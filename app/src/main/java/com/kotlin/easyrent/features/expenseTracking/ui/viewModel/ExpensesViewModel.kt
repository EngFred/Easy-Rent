package com.kotlin.easyrent.features.expenseTracking.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.easyrent.features.expenseTracking.domain.modal.Expense
import com.kotlin.easyrent.features.expenseTracking.domain.usecase.DeleteExpenseUseCase
import com.kotlin.easyrent.features.expenseTracking.domain.usecase.GetAllExpenseUseCase
import com.kotlin.easyrent.features.expenseTracking.ui.screens.expenses.ExpensesUIState
import com.kotlin.easyrent.features.expenseTracking.ui.screens.expenses.ExpensesUiEvents
import com.kotlin.easyrent.utils.ServiceResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val getAllExpenseUseCase: GetAllExpenseUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpensesUIState())
    val uiState = _uiState.asStateFlow()

    init {
        getExpenses()
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
        }
    }

    private fun getExpenses()  = viewModelScope.launch {
        getAllExpenseUseCase().collect { result ->
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
                            expenses = result.data
                        )
                    }
                }
            }
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