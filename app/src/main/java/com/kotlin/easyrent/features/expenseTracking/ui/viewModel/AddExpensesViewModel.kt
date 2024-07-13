package com.kotlin.easyrent.features.expenseTracking.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.easyrent.R
import com.kotlin.easyrent.features.expenseTracking.domain.modal.Expense
import com.kotlin.easyrent.features.expenseTracking.domain.usecase.AddExpenseUseCase
import com.kotlin.easyrent.features.expenseTracking.ui.screens.add.AddExpenseUiEvents
import com.kotlin.easyrent.features.expenseTracking.ui.screens.add.AddExpenseUiState
import com.kotlin.easyrent.features.rentalManagement.domain.usecase.GetAllRentalsUseCase
import com.kotlin.easyrent.utils.ServiceResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddExpensesViewModel @Inject constructor(
    private val getAllRentalsUseCase: GetAllRentalsUseCase,
    private val addExpenseUseCase: AddExpenseUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddExpenseUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getAllRentals()
    }

    fun onEvent( event: AddExpenseUiEvents) {
        if ( _uiState.value.addError != null ) {
            _uiState.update {
                it.copy(
                    addError = null
                )
            }
        }
        when(event) {
            is AddExpenseUiEvents.AmountChanged -> {
                _uiState.update {
                    it.copy(
                        amount = event.amount
                    )
                }
                validateAmount(_uiState.value.amount)
            }
            is AddExpenseUiEvents.DescriptionChanged -> {
                _uiState.update {
                    it.copy(
                        description = event.description
                    )
                }
                validateDescription(_uiState.value.description)
            }
            is AddExpenseUiEvents.RentalChanged -> {
                _uiState.update {
                    it.copy(
                        selectedRental = event.rental,
                        showRentalsDialog = false
                    )
                }
            }
            AddExpenseUiEvents.SaveButtonClicked -> {
                if (_uiState.value.isFormValid) {
                    saveExpense()
                }
            }

            AddExpenseUiEvents.ChooseRentalClicked -> {
                _uiState.update {
                    it.copy(
                        showRentalsDialog = !it.showRentalsDialog
                    )
                }
            }
        }
        validateForm()
    }
    private fun getAllRentals() = viewModelScope.launch {
        getAllRentalsUseCase.invoke().collectLatest { result ->
            when(result) {
                is ServiceResponse.Error -> {
                    Log.e("%", "Failed tot get rentals: ${result.message}")
                }
                ServiceResponse.Idle -> Unit
                is ServiceResponse.Success -> {
                    _uiState.update {
                        it.copy(
                            rentals = result.data,
                            selectedRental = if ( result.data.size == 1 ) result.data[0] else it.selectedRental
                        )
                    }
                }
            }
        }
    }

    private fun saveExpense() = viewModelScope.launch( Dispatchers.IO ) {
        _uiState.update {
            it.copy(
                isAdding = true
            )
        }
        val expense = Expense(
            id = UUID.randomUUID().toString(),
            rentalId = _uiState.value.selectedRental!!.id,
            rentalName = _uiState.value.selectedRental!!.name,
            amount = _uiState.value.amount.toDouble(),
            description = _uiState.value.description.trim()
        )
        val result = addExpenseUseCase.invoke(expense)
        Log.v("TAG", "Task done!")
        when(result){
            is ServiceResponse.Error -> {
                _uiState.update {
                    it.copy(
                        isAdding = false,
                        addError = result.message
                    )
                }
            }
            ServiceResponse.Idle -> Unit
            is ServiceResponse.Success -> {
                _uiState.update {
                    it.copy(
                        addSuccessful = true
                    )
                }
            }
        }
    }


    private fun validateForm() {
        val amountValidationResults = _uiState.value.amount.isNotEmpty() && _uiState.value.amountError == null
        val descriptionValidationResults = _uiState.value.description.isNotEmpty() && _uiState.value.descriptionError == null
        val rentalValidationResults = _uiState.value.selectedRental != null

        Log.i("TAG", "amountValidationResults: $amountValidationResults")
        Log.i("TAG", "descriptionValidationResults: $descriptionValidationResults")
        Log.i("TAG", "rentalValidationResults: $rentalValidationResults")

        val isFormValid = amountValidationResults && descriptionValidationResults && rentalValidationResults

        _uiState.update {
            it.copy(
                isFormValid = isFormValid
            )
        }
    }


    private fun  validateAmount(amount: String) {
        val amountError = when {
            amount.isEmpty() -> R.string.empty_amount
            amount.toIntOrNull() == null || amount == "0" -> R.string.invalid_amount
            else -> null
        }
        _uiState.update {
            it.copy(
                amountError = amountError
            )
        }
    }

    private fun  validateDescription(description: String) {
        val descriptionError = when {
            description.isEmpty() -> R.string.empty_description
            description.length > 18 -> R.string.long_description
            else -> null
        }
        _uiState.update {
            it.copy(
                descriptionError = descriptionError
            )
        }
    }
}