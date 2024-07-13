package com.kotlin.easyrent.features.expenseTracking.ui.screens.add

import androidx.annotation.StringRes
import com.kotlin.easyrent.features.rentalManagement.domain.modal.Rental

data class AddExpenseUiState(
    val amount: String = "",
    @StringRes
    val amountError: Int? =null,
    val description: String = "",
    @StringRes
    val descriptionError: Int? =null,

    val selectedRental: Rental? = null,
    val rentals: List<Rental> = emptyList(),
    val showRentalsDialog: Boolean = false,
    val isFormValid: Boolean = false,

    val isAdding: Boolean = false,
    @StringRes
    val addError: Int? = null,
    val addSuccessful: Boolean = false
)