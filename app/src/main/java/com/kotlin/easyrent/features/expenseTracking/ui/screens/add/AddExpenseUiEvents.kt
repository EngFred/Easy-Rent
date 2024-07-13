package com.kotlin.easyrent.features.expenseTracking.ui.screens.add

import com.kotlin.easyrent.features.rentalManagement.domain.modal.Rental

sealed class AddExpenseUiEvents {
    data class AmountChanged( val amount: String ) : AddExpenseUiEvents()
    data class DescriptionChanged( val description: String ) : AddExpenseUiEvents()

    data class RentalChanged( val rental: Rental ) : AddExpenseUiEvents()

    data object SaveButtonClicked: AddExpenseUiEvents()
    data object ChooseRentalClicked: AddExpenseUiEvents()
}