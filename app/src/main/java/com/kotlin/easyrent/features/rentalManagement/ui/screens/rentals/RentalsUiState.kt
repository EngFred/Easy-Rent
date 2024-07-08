package com.kotlin.easyrent.features.rentalManagement.ui.screens.rentals

import androidx.annotation.StringRes
import com.kotlin.easyrent.features.rentalManagement.domain.modal.Rental

data class RentalsUiState(
    val isLoading: Boolean = true,
    @StringRes
    val loadError: Int? = null,
    val rentals: List<Rental> = emptyList(),
    val deletedRentalId: String = "",
    val deletingRental: Boolean = false,
    @StringRes
    val rentalDeleteError: Int? = null
)
