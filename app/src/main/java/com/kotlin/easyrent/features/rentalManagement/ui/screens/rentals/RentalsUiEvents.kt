package com.kotlin.easyrent.features.rentalManagement.ui.screens.rentals

import com.kotlin.easyrent.features.rentalManagement.domain.modal.Rental

sealed class RentalsUiEvents {
    data class RentalDeleted(val rental: Rental ) : RentalsUiEvents()
}